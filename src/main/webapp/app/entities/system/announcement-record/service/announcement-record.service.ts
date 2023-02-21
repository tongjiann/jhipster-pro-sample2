import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnnouncementRecord, getAnnouncementRecordIdentifier } from '../announcement-record.model';

export type EntityResponseType = HttpResponse<IAnnouncementRecord>;
export type EntityArrayResponseType = HttpResponse<IAnnouncementRecord[]>;

@Injectable({ providedIn: 'root' })
export class AnnouncementRecordService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/announcement-records');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(announcementRecord: IAnnouncementRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(announcementRecord);
    return this.http
      .post<IAnnouncementRecord>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(announcementRecord: IAnnouncementRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(announcementRecord);
    return this.http
      .put<IAnnouncementRecord>(`${this.resourceUrl}/${getAnnouncementRecordIdentifier(announcementRecord) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(announcementRecord: IAnnouncementRecord, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(announcementRecord);
    const copy = announcementRecord;
    return this.http
      .put<IAnnouncementRecord>(
        this.resourceUrl + '/specified-fields',
        { announcementRecord: copy, specifiedFields },
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAnnouncementRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnnouncementRecord[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  deleteByIds(ids: number[]): Observable<HttpResponse<any>> {
    let options: HttpParams = new HttpParams();
    ids.forEach(id => {
      options = options.append('ids', String(id));
    });
    return this.http.delete<any>(`${this.resourceUrl}`, { params: options, observe: 'response' });
  }

  addAnnouncementRecordToCollectionIfMissing(
    announcementRecordCollection: IAnnouncementRecord[],
    ...announcementRecordsToCheck: (IAnnouncementRecord | null | undefined)[]
  ): IAnnouncementRecord[] {
    const announcementRecords: IAnnouncementRecord[] = announcementRecordsToCheck.filter(isPresent);
    if (announcementRecords.length > 0) {
      const announcementRecordCollectionIdentifiers = announcementRecordCollection.map(
        announcementRecordItem => getAnnouncementRecordIdentifier(announcementRecordItem)!
      );
      const announcementRecordsToAdd = announcementRecords.filter(announcementRecordItem => {
        const announcementRecordIdentifier = getAnnouncementRecordIdentifier(announcementRecordItem);
        if (announcementRecordIdentifier == null || announcementRecordCollectionIdentifiers.includes(announcementRecordIdentifier)) {
          return false;
        }
        announcementRecordCollectionIdentifiers.push(announcementRecordIdentifier);
        return true;
      });
      return [...announcementRecordsToAdd, ...announcementRecordCollection];
    }
    return announcementRecordCollection;
  }

  protected convertDateFromClient(announcementRecord: IAnnouncementRecord): IAnnouncementRecord {
    return Object.assign({}, announcementRecord, {
      readTime: announcementRecord.readTime?.isValid() ? announcementRecord.readTime.toJSON() : undefined,
      removedAt: announcementRecord.removedAt?.isValid() ? announcementRecord.removedAt.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.readTime = res.body.readTime ? dayjs(res.body.readTime) : undefined;
      res.body.removedAt = res.body.removedAt ? dayjs(res.body.removedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((announcementRecord: IAnnouncementRecord) => {
        announcementRecord.readTime = announcementRecord.readTime ? dayjs(announcementRecord.readTime) : undefined;
        announcementRecord.removedAt = announcementRecord.removedAt ? dayjs(announcementRecord.removedAt) : undefined;
      });
    }
    return res;
  }
}
