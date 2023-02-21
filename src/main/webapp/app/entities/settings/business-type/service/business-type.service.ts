import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBusinessType, getBusinessTypeIdentifier } from '../business-type.model';

export type EntityResponseType = HttpResponse<IBusinessType>;
export type EntityArrayResponseType = HttpResponse<IBusinessType[]>;

@Injectable({ providedIn: 'root' })
export class BusinessTypeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/business-types');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(businessType: IBusinessType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(businessType);
    return this.http
      .post<IBusinessType>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(businessType: IBusinessType): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(businessType);
    return this.http
      .put<IBusinessType>(`${this.resourceUrl}/${getBusinessTypeIdentifier(businessType) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(businessType: IBusinessType, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(businessType);
    const copy = businessType;
    return this.http
      .put<IBusinessType>(this.resourceUrl + '/specified-fields', { businessType: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBusinessType>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBusinessType[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addBusinessTypeToCollectionIfMissing(
    businessTypeCollection: IBusinessType[],
    ...businessTypesToCheck: (IBusinessType | null | undefined)[]
  ): IBusinessType[] {
    const businessTypes: IBusinessType[] = businessTypesToCheck.filter(isPresent);
    if (businessTypes.length > 0) {
      const businessTypeCollectionIdentifiers = businessTypeCollection.map(
        businessTypeItem => getBusinessTypeIdentifier(businessTypeItem)!
      );
      const businessTypesToAdd = businessTypes.filter(businessTypeItem => {
        const businessTypeIdentifier = getBusinessTypeIdentifier(businessTypeItem);
        if (businessTypeIdentifier == null || businessTypeCollectionIdentifiers.includes(businessTypeIdentifier)) {
          return false;
        }
        businessTypeCollectionIdentifiers.push(businessTypeIdentifier);
        return true;
      });
      return [...businessTypesToAdd, ...businessTypeCollection];
    }
    return businessTypeCollection;
  }

  protected convertDateFromClient(businessType: IBusinessType): IBusinessType {
    return Object.assign({}, businessType, {
      removedAt: businessType.removedAt?.isValid() ? businessType.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((businessType: IBusinessType) => {
        businessType.removedAt = businessType.removedAt ? dayjs(businessType.removedAt) : undefined;
      });
    }
    return res;
  }
}
