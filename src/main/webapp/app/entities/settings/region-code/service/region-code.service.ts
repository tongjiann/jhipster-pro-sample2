import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRegionCode, getRegionCodeIdentifier } from '../region-code.model';

export type EntityResponseType = HttpResponse<IRegionCode>;
export type EntityArrayResponseType = HttpResponse<IRegionCode[]>;

@Injectable({ providedIn: 'root' })
export class RegionCodeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/region-codes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(regionCode: IRegionCode): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(regionCode);
    return this.http
      .post<IRegionCode>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(regionCode: IRegionCode): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(regionCode);
    return this.http
      .put<IRegionCode>(`${this.resourceUrl}/${getRegionCodeIdentifier(regionCode) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(regionCode: IRegionCode, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(regionCode);
    const copy = regionCode;
    return this.http
      .put<IRegionCode>(this.resourceUrl + '/specified-fields', { regionCode: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRegionCode>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRegionCode[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  tree(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRegionCode[]>(this.resourceUrl + '/tree', { params: options, observe: 'response' })
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

  addRegionCodeToCollectionIfMissing(
    regionCodeCollection: IRegionCode[],
    ...regionCodesToCheck: (IRegionCode | null | undefined)[]
  ): IRegionCode[] {
    const regionCodes: IRegionCode[] = regionCodesToCheck.filter(isPresent);
    if (regionCodes.length > 0) {
      const regionCodeCollectionIdentifiers = regionCodeCollection.map(regionCodeItem => getRegionCodeIdentifier(regionCodeItem)!);
      const regionCodesToAdd = regionCodes.filter(regionCodeItem => {
        const regionCodeIdentifier = getRegionCodeIdentifier(regionCodeItem);
        if (regionCodeIdentifier == null || regionCodeCollectionIdentifiers.includes(regionCodeIdentifier)) {
          return false;
        }
        regionCodeCollectionIdentifiers.push(regionCodeIdentifier);
        return true;
      });
      return [...regionCodesToAdd, ...regionCodeCollection];
    }
    return regionCodeCollection;
  }

  protected convertDateFromClient(regionCode: IRegionCode): IRegionCode {
    return Object.assign({}, regionCode, {
      removedAt: regionCode.removedAt?.isValid() ? regionCode.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((regionCode: IRegionCode) => {
        regionCode.removedAt = regionCode.removedAt ? dayjs(regionCode.removedAt) : undefined;
      });
    }
    return res;
  }
}
