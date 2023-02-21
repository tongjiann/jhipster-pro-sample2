import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAuthority, getAuthorityIdentifier } from '../authority.model';

export type EntityResponseType = HttpResponse<IAuthority>;
export type EntityArrayResponseType = HttpResponse<IAuthority[]>;

@Injectable({ providedIn: 'root' })
export class AuthorityService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/authorities');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(authority: IAuthority): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(authority);
    return this.http
      .post<IAuthority>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(authority: IAuthority): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(authority);
    return this.http
      .put<IAuthority>(`${this.resourceUrl}/${getAuthorityIdentifier(authority) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(authority: IAuthority, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(authority);
    const copy = authority;
    return this.http
      .put<IAuthority>(this.resourceUrl + '/specified-fields', { authority: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAuthority>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAuthority[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  tree(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAuthority[]>(this.resourceUrl + '/tree', { params: options, observe: 'response' })
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

  addAuthorityToCollectionIfMissing(
    authorityCollection: IAuthority[],
    ...authoritiesToCheck: (IAuthority | null | undefined)[]
  ): IAuthority[] {
    const authorities: IAuthority[] = authoritiesToCheck.filter(isPresent);
    if (authorities.length > 0) {
      const authorityCollectionIdentifiers = authorityCollection.map(authorityItem => getAuthorityIdentifier(authorityItem)!);
      const authoritiesToAdd = authorities.filter(authorityItem => {
        const authorityIdentifier = getAuthorityIdentifier(authorityItem);
        if (authorityIdentifier == null || authorityCollectionIdentifiers.includes(authorityIdentifier)) {
          return false;
        }
        authorityCollectionIdentifiers.push(authorityIdentifier);
        return true;
      });
      return [...authoritiesToAdd, ...authorityCollection];
    }
    return authorityCollection;
  }

  protected convertDateFromClient(authority: IAuthority): IAuthority {
    return Object.assign({}, authority, {
      removedAt: authority.removedAt?.isValid() ? authority.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((authority: IAuthority) => {
        authority.removedAt = authority.removedAt ? dayjs(authority.removedAt) : undefined;
      });
    }
    return res;
  }
}
