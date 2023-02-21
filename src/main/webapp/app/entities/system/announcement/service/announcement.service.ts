import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnnouncement, getAnnouncementIdentifier } from '../announcement.model';

export type EntityResponseType = HttpResponse<IAnnouncement>;
export type EntityArrayResponseType = HttpResponse<IAnnouncement[]>;

@Injectable({ providedIn: 'root' })
export class AnnouncementService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/announcements');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(announcement: IAnnouncement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(announcement);
    return this.http
      .post<IAnnouncement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(announcement: IAnnouncement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(announcement);
    return this.http
      .put<IAnnouncement>(`${this.resourceUrl}/${getAnnouncementIdentifier(announcement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(announcement: IAnnouncement, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(announcement);
    const copy = announcement;
    return this.http
      .put<IAnnouncement>(this.resourceUrl + '/specified-fields', { announcement: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAnnouncement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnnouncement[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addAnnouncementToCollectionIfMissing(
    announcementCollection: IAnnouncement[],
    ...announcementsToCheck: (IAnnouncement | null | undefined)[]
  ): IAnnouncement[] {
    const announcements: IAnnouncement[] = announcementsToCheck.filter(isPresent);
    if (announcements.length > 0) {
      const announcementCollectionIdentifiers = announcementCollection.map(
        announcementItem => getAnnouncementIdentifier(announcementItem)!
      );
      const announcementsToAdd = announcements.filter(announcementItem => {
        const announcementIdentifier = getAnnouncementIdentifier(announcementItem);
        if (announcementIdentifier == null || announcementCollectionIdentifiers.includes(announcementIdentifier)) {
          return false;
        }
        announcementCollectionIdentifiers.push(announcementIdentifier);
        return true;
      });
      return [...announcementsToAdd, ...announcementCollection];
    }
    return announcementCollection;
  }

  protected convertDateFromClient(announcement: IAnnouncement): IAnnouncement {
    return Object.assign({}, announcement, {
      startTime: announcement.startTime?.isValid() ? announcement.startTime.toJSON() : undefined,
      endTime: announcement.endTime?.isValid() ? announcement.endTime.toJSON() : undefined,
      sendTime: announcement.sendTime?.isValid() ? announcement.sendTime.toJSON() : undefined,
      cancelTime: announcement.cancelTime?.isValid() ? announcement.cancelTime.toJSON() : undefined,
      removedAt: announcement.removedAt?.isValid() ? announcement.removedAt.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startTime = res.body.startTime ? dayjs(res.body.startTime) : undefined;
      res.body.endTime = res.body.endTime ? dayjs(res.body.endTime) : undefined;
      res.body.sendTime = res.body.sendTime ? dayjs(res.body.sendTime) : undefined;
      res.body.cancelTime = res.body.cancelTime ? dayjs(res.body.cancelTime) : undefined;
      res.body.removedAt = res.body.removedAt ? dayjs(res.body.removedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((announcement: IAnnouncement) => {
        announcement.startTime = announcement.startTime ? dayjs(announcement.startTime) : undefined;
        announcement.endTime = announcement.endTime ? dayjs(announcement.endTime) : undefined;
        announcement.sendTime = announcement.sendTime ? dayjs(announcement.sendTime) : undefined;
        announcement.cancelTime = announcement.cancelTime ? dayjs(announcement.cancelTime) : undefined;
        announcement.removedAt = announcement.removedAt ? dayjs(announcement.removedAt) : undefined;
      });
    }
    return res;
  }
}
