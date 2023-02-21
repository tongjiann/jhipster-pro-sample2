import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUReportFile, getUReportFileIdentifier } from '../u-report-file.model';

export type EntityResponseType = HttpResponse<IUReportFile>;
export type EntityArrayResponseType = HttpResponse<IUReportFile[]>;

@Injectable({ providedIn: 'root' })
export class UReportFileService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/u-report-files');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(uReportFile: IUReportFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(uReportFile);
    return this.http
      .post<IUReportFile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(uReportFile: IUReportFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(uReportFile);
    return this.http
      .put<IUReportFile>(`${this.resourceUrl}/${getUReportFileIdentifier(uReportFile) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(uReportFile: IUReportFile, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(uReportFile);
    const copy = uReportFile;
    return this.http
      .put<IUReportFile>(this.resourceUrl + '/specified-fields', { uReportFile: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUReportFile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUReportFile[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addUReportFileToCollectionIfMissing(
    uReportFileCollection: IUReportFile[],
    ...uReportFilesToCheck: (IUReportFile | null | undefined)[]
  ): IUReportFile[] {
    const uReportFiles: IUReportFile[] = uReportFilesToCheck.filter(isPresent);
    if (uReportFiles.length > 0) {
      const uReportFileCollectionIdentifiers = uReportFileCollection.map(uReportFileItem => getUReportFileIdentifier(uReportFileItem)!);
      const uReportFilesToAdd = uReportFiles.filter(uReportFileItem => {
        const uReportFileIdentifier = getUReportFileIdentifier(uReportFileItem);
        if (uReportFileIdentifier == null || uReportFileCollectionIdentifiers.includes(uReportFileIdentifier)) {
          return false;
        }
        uReportFileCollectionIdentifiers.push(uReportFileIdentifier);
        return true;
      });
      return [...uReportFilesToAdd, ...uReportFileCollection];
    }
    return uReportFileCollection;
  }

  protected convertDateFromClient(uReportFile: IUReportFile): IUReportFile {
    return Object.assign({}, uReportFile, {
      createAt: uReportFile.createAt?.isValid() ? uReportFile.createAt.toJSON() : undefined,
      updateAt: uReportFile.updateAt?.isValid() ? uReportFile.updateAt.toJSON() : undefined,
      removedAt: uReportFile.removedAt?.isValid() ? uReportFile.removedAt.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createAt = res.body.createAt ? dayjs(res.body.createAt) : undefined;
      res.body.updateAt = res.body.updateAt ? dayjs(res.body.updateAt) : undefined;
      res.body.removedAt = res.body.removedAt ? dayjs(res.body.removedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((uReportFile: IUReportFile) => {
        uReportFile.createAt = uReportFile.createAt ? dayjs(uReportFile.createAt) : undefined;
        uReportFile.updateAt = uReportFile.updateAt ? dayjs(uReportFile.updateAt) : undefined;
        uReportFile.removedAt = uReportFile.removedAt ? dayjs(uReportFile.removedAt) : undefined;
      });
    }
    return res;
  }
}
