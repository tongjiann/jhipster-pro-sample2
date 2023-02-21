import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApiPermission, getApiPermissionIdentifier } from '../api-permission.model';

export type EntityResponseType = HttpResponse<IApiPermission>;
export type EntityArrayResponseType = HttpResponse<IApiPermission[]>;

@Injectable({ providedIn: 'root' })
export class ApiPermissionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/api-permissions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(apiPermission: IApiPermission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apiPermission);
    return this.http
      .post<IApiPermission>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(apiPermission: IApiPermission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apiPermission);
    return this.http
      .put<IApiPermission>(`${this.resourceUrl}/${getApiPermissionIdentifier(apiPermission) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(apiPermission: IApiPermission, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(apiPermission);
    const copy = apiPermission;
    return this.http
      .put<IApiPermission>(this.resourceUrl + '/specified-fields', { apiPermission: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IApiPermission>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IApiPermission[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  tree(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IApiPermission[]>(this.resourceUrl + '/tree', { params: options, observe: 'response' })
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

  addApiPermissionToCollectionIfMissing(
    apiPermissionCollection: IApiPermission[],
    ...apiPermissionsToCheck: (IApiPermission | null | undefined)[]
  ): IApiPermission[] {
    const apiPermissions: IApiPermission[] = apiPermissionsToCheck.filter(isPresent);
    if (apiPermissions.length > 0) {
      const apiPermissionCollectionIdentifiers = apiPermissionCollection.map(
        apiPermissionItem => getApiPermissionIdentifier(apiPermissionItem)!
      );
      const apiPermissionsToAdd = apiPermissions.filter(apiPermissionItem => {
        const apiPermissionIdentifier = getApiPermissionIdentifier(apiPermissionItem);
        if (apiPermissionIdentifier == null || apiPermissionCollectionIdentifiers.includes(apiPermissionIdentifier)) {
          return false;
        }
        apiPermissionCollectionIdentifiers.push(apiPermissionIdentifier);
        return true;
      });
      return [...apiPermissionsToAdd, ...apiPermissionCollection];
    }
    return apiPermissionCollection;
  }

  protected convertDateFromClient(apiPermission: IApiPermission): IApiPermission {
    return Object.assign({}, apiPermission, {
      removedAt: apiPermission.removedAt?.isValid() ? apiPermission.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((apiPermission: IApiPermission) => {
        apiPermission.removedAt = apiPermission.removedAt ? dayjs(apiPermission.removedAt) : undefined;
      });
    }
    return res;
  }
}
