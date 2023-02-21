import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISmsConfig, getSmsConfigIdentifier } from '../sms-config.model';

export type EntityResponseType = HttpResponse<ISmsConfig>;
export type EntityArrayResponseType = HttpResponse<ISmsConfig[]>;

@Injectable({ providedIn: 'root' })
export class SmsConfigService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/sms-configs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(smsConfig: ISmsConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(smsConfig);
    return this.http
      .post<ISmsConfig>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(smsConfig: ISmsConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(smsConfig);
    return this.http
      .put<ISmsConfig>(`${this.resourceUrl}/${getSmsConfigIdentifier(smsConfig) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(smsConfig: ISmsConfig, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(smsConfig);
    const copy = smsConfig;
    return this.http
      .put<ISmsConfig>(this.resourceUrl + '/specified-fields', { smsConfig: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISmsConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISmsConfig[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addSmsConfigToCollectionIfMissing(
    smsConfigCollection: ISmsConfig[],
    ...smsConfigsToCheck: (ISmsConfig | null | undefined)[]
  ): ISmsConfig[] {
    const smsConfigs: ISmsConfig[] = smsConfigsToCheck.filter(isPresent);
    if (smsConfigs.length > 0) {
      const smsConfigCollectionIdentifiers = smsConfigCollection.map(smsConfigItem => getSmsConfigIdentifier(smsConfigItem)!);
      const smsConfigsToAdd = smsConfigs.filter(smsConfigItem => {
        const smsConfigIdentifier = getSmsConfigIdentifier(smsConfigItem);
        if (smsConfigIdentifier == null || smsConfigCollectionIdentifiers.includes(smsConfigIdentifier)) {
          return false;
        }
        smsConfigCollectionIdentifiers.push(smsConfigIdentifier);
        return true;
      });
      return [...smsConfigsToAdd, ...smsConfigCollection];
    }
    return smsConfigCollection;
  }

  protected convertDateFromClient(smsConfig: ISmsConfig): ISmsConfig {
    return Object.assign({}, smsConfig, {
      removedAt: smsConfig.removedAt?.isValid() ? smsConfig.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((smsConfig: ISmsConfig) => {
        smsConfig.removedAt = smsConfig.removedAt ? dayjs(smsConfig.removedAt) : undefined;
      });
    }
    return res;
  }
}
