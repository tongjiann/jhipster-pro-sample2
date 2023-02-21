import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISysLog, getSysLogIdentifier } from '../sys-log.model';

export type EntityResponseType = HttpResponse<ISysLog>;
export type EntityArrayResponseType = HttpResponse<ISysLog[]>;

@Injectable({ providedIn: 'root' })
export class SysLogService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/sys-logs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(sysLog: ISysLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sysLog);
    return this.http
      .post<ISysLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sysLog: ISysLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sysLog);
    return this.http
      .put<ISysLog>(`${this.resourceUrl}/${getSysLogIdentifier(sysLog) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(sysLog: ISysLog, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(sysLog);
    const copy = sysLog;
    return this.http
      .put<ISysLog>(this.resourceUrl + '/specified-fields', { sysLog: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISysLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISysLog[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addSysLogToCollectionIfMissing(sysLogCollection: ISysLog[], ...sysLogsToCheck: (ISysLog | null | undefined)[]): ISysLog[] {
    const sysLogs: ISysLog[] = sysLogsToCheck.filter(isPresent);
    if (sysLogs.length > 0) {
      const sysLogCollectionIdentifiers = sysLogCollection.map(sysLogItem => getSysLogIdentifier(sysLogItem)!);
      const sysLogsToAdd = sysLogs.filter(sysLogItem => {
        const sysLogIdentifier = getSysLogIdentifier(sysLogItem);
        if (sysLogIdentifier == null || sysLogCollectionIdentifiers.includes(sysLogIdentifier)) {
          return false;
        }
        sysLogCollectionIdentifiers.push(sysLogIdentifier);
        return true;
      });
      return [...sysLogsToAdd, ...sysLogCollection];
    }
    return sysLogCollection;
  }

  protected convertDateFromClient(sysLog: ISysLog): ISysLog {
    return Object.assign({}, sysLog, {
      removedAt: sysLog.removedAt?.isValid() ? sysLog.removedAt.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.removedAt = res.body.removedAt ? dayjs(res.body.removedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((sysLog: ISysLog) => {
        sysLog.removedAt = sysLog.removedAt ? dayjs(sysLog.removedAt) : undefined;
      });
    }
    return res;
  }
}
