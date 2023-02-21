import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IViewPermission, getViewPermissionIdentifier } from '../view-permission.model';

export type EntityResponseType = HttpResponse<IViewPermission>;
export type EntityArrayResponseType = HttpResponse<IViewPermission[]>;

@Injectable({ providedIn: 'root' })
export class ViewPermissionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/view-permissions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(viewPermission: IViewPermission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(viewPermission);
    return this.http
      .post<IViewPermission>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(viewPermission: IViewPermission): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(viewPermission);
    return this.http
      .put<IViewPermission>(`${this.resourceUrl}/${getViewPermissionIdentifier(viewPermission) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(viewPermission: IViewPermission, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(viewPermission);
    const copy = viewPermission;
    return this.http
      .put<IViewPermission>(this.resourceUrl + '/specified-fields', { viewPermission: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IViewPermission>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IViewPermission[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  tree(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IViewPermission[]>(this.resourceUrl + '/tree', { params: options, observe: 'response' })
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

  addViewPermissionToCollectionIfMissing(
    viewPermissionCollection: IViewPermission[],
    ...viewPermissionsToCheck: (IViewPermission | null | undefined)[]
  ): IViewPermission[] {
    const viewPermissions: IViewPermission[] = viewPermissionsToCheck.filter(isPresent);
    if (viewPermissions.length > 0) {
      const viewPermissionCollectionIdentifiers = viewPermissionCollection.map(
        viewPermissionItem => getViewPermissionIdentifier(viewPermissionItem)!
      );
      const viewPermissionsToAdd = viewPermissions.filter(viewPermissionItem => {
        const viewPermissionIdentifier = getViewPermissionIdentifier(viewPermissionItem);
        if (viewPermissionIdentifier == null || viewPermissionCollectionIdentifiers.includes(viewPermissionIdentifier)) {
          return false;
        }
        viewPermissionCollectionIdentifiers.push(viewPermissionIdentifier);
        return true;
      });
      return [...viewPermissionsToAdd, ...viewPermissionCollection];
    }
    return viewPermissionCollection;
  }

  protected convertDateFromClient(viewPermission: IViewPermission): IViewPermission {
    return Object.assign({}, viewPermission, {
      removedAt: viewPermission.removedAt?.isValid() ? viewPermission.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((viewPermission: IViewPermission) => {
        viewPermission.removedAt = viewPermission.removedAt ? dayjs(viewPermission.removedAt) : undefined;
      });
    }
    return res;
  }
}
