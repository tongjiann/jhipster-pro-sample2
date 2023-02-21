import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISiteConfig, getSiteConfigIdentifier } from '../site-config.model';

export type EntityResponseType = HttpResponse<ISiteConfig>;
export type EntityArrayResponseType = HttpResponse<ISiteConfig[]>;

@Injectable({ providedIn: 'root' })
export class SiteConfigService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/site-configs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(siteConfig: ISiteConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(siteConfig);
    return this.http
      .post<ISiteConfig>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(siteConfig: ISiteConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(siteConfig);
    return this.http
      .put<ISiteConfig>(`${this.resourceUrl}/${getSiteConfigIdentifier(siteConfig) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(siteConfig: ISiteConfig, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(siteConfig);
    const copy = siteConfig;
    return this.http
      .put<ISiteConfig>(this.resourceUrl + '/specified-fields', { siteConfig: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISiteConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISiteConfig[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addSiteConfigToCollectionIfMissing(
    siteConfigCollection: ISiteConfig[],
    ...siteConfigsToCheck: (ISiteConfig | null | undefined)[]
  ): ISiteConfig[] {
    const siteConfigs: ISiteConfig[] = siteConfigsToCheck.filter(isPresent);
    if (siteConfigs.length > 0) {
      const siteConfigCollectionIdentifiers = siteConfigCollection.map(siteConfigItem => getSiteConfigIdentifier(siteConfigItem)!);
      const siteConfigsToAdd = siteConfigs.filter(siteConfigItem => {
        const siteConfigIdentifier = getSiteConfigIdentifier(siteConfigItem);
        if (siteConfigIdentifier == null || siteConfigCollectionIdentifiers.includes(siteConfigIdentifier)) {
          return false;
        }
        siteConfigCollectionIdentifiers.push(siteConfigIdentifier);
        return true;
      });
      return [...siteConfigsToAdd, ...siteConfigCollection];
    }
    return siteConfigCollection;
  }

  protected convertDateFromClient(siteConfig: ISiteConfig): ISiteConfig {
    return Object.assign({}, siteConfig, {
      removedAt: siteConfig.removedAt?.isValid() ? siteConfig.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((siteConfig: ISiteConfig) => {
        siteConfig.removedAt = siteConfig.removedAt ? dayjs(siteConfig.removedAt) : undefined;
      });
    }
    return res;
  }
}
