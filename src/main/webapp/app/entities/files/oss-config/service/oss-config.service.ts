import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOssConfig, getOssConfigIdentifier } from '../oss-config.model';

export type EntityResponseType = HttpResponse<IOssConfig>;
export type EntityArrayResponseType = HttpResponse<IOssConfig[]>;

@Injectable({ providedIn: 'root' })
export class OssConfigService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/oss-configs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(ossConfig: IOssConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ossConfig);
    return this.http
      .post<IOssConfig>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ossConfig: IOssConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ossConfig);
    return this.http
      .put<IOssConfig>(`${this.resourceUrl}/${getOssConfigIdentifier(ossConfig) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(ossConfig: IOssConfig, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(ossConfig);
    const copy = ossConfig;
    return this.http
      .put<IOssConfig>(this.resourceUrl + '/specified-fields', { ossConfig: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOssConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOssConfig[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addOssConfigToCollectionIfMissing(
    ossConfigCollection: IOssConfig[],
    ...ossConfigsToCheck: (IOssConfig | null | undefined)[]
  ): IOssConfig[] {
    const ossConfigs: IOssConfig[] = ossConfigsToCheck.filter(isPresent);
    if (ossConfigs.length > 0) {
      const ossConfigCollectionIdentifiers = ossConfigCollection.map(ossConfigItem => getOssConfigIdentifier(ossConfigItem)!);
      const ossConfigsToAdd = ossConfigs.filter(ossConfigItem => {
        const ossConfigIdentifier = getOssConfigIdentifier(ossConfigItem);
        if (ossConfigIdentifier == null || ossConfigCollectionIdentifiers.includes(ossConfigIdentifier)) {
          return false;
        }
        ossConfigCollectionIdentifiers.push(ossConfigIdentifier);
        return true;
      });
      return [...ossConfigsToAdd, ...ossConfigCollection];
    }
    return ossConfigCollection;
  }

  protected convertDateFromClient(ossConfig: IOssConfig): IOssConfig {
    return Object.assign({}, ossConfig, {
      removedAt: ossConfig.removedAt?.isValid() ? ossConfig.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((ossConfig: IOssConfig) => {
        ossConfig.removedAt = ossConfig.removedAt ? dayjs(ossConfig.removedAt) : undefined;
      });
    }
    return res;
  }
}
