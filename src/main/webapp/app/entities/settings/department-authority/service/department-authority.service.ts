import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDepartmentAuthority, getDepartmentAuthorityIdentifier } from '../department-authority.model';

export type EntityResponseType = HttpResponse<IDepartmentAuthority>;
export type EntityArrayResponseType = HttpResponse<IDepartmentAuthority[]>;

@Injectable({ providedIn: 'root' })
export class DepartmentAuthorityService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/department-authorities');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(departmentAuthority: IDepartmentAuthority): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(departmentAuthority);
    return this.http
      .post<IDepartmentAuthority>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(departmentAuthority: IDepartmentAuthority): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(departmentAuthority);
    return this.http
      .put<IDepartmentAuthority>(`${this.resourceUrl}/${getDepartmentAuthorityIdentifier(departmentAuthority) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(departmentAuthority: IDepartmentAuthority, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(departmentAuthority);
    const copy = departmentAuthority;
    return this.http
      .put<IDepartmentAuthority>(
        this.resourceUrl + '/specified-fields',
        { departmentAuthority: copy, specifiedFields },
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDepartmentAuthority>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepartmentAuthority[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addDepartmentAuthorityToCollectionIfMissing(
    departmentAuthorityCollection: IDepartmentAuthority[],
    ...departmentAuthoritiesToCheck: (IDepartmentAuthority | null | undefined)[]
  ): IDepartmentAuthority[] {
    const departmentAuthorities: IDepartmentAuthority[] = departmentAuthoritiesToCheck.filter(isPresent);
    if (departmentAuthorities.length > 0) {
      const departmentAuthorityCollectionIdentifiers = departmentAuthorityCollection.map(
        departmentAuthorityItem => getDepartmentAuthorityIdentifier(departmentAuthorityItem)!
      );
      const departmentAuthoritiesToAdd = departmentAuthorities.filter(departmentAuthorityItem => {
        const departmentAuthorityIdentifier = getDepartmentAuthorityIdentifier(departmentAuthorityItem);
        if (departmentAuthorityIdentifier == null || departmentAuthorityCollectionIdentifiers.includes(departmentAuthorityIdentifier)) {
          return false;
        }
        departmentAuthorityCollectionIdentifiers.push(departmentAuthorityIdentifier);
        return true;
      });
      return [...departmentAuthoritiesToAdd, ...departmentAuthorityCollection];
    }
    return departmentAuthorityCollection;
  }

  protected convertDateFromClient(departmentAuthority: IDepartmentAuthority): IDepartmentAuthority {
    return Object.assign({}, departmentAuthority, {
      createTime: departmentAuthority.createTime?.isValid() ? departmentAuthority.createTime.toJSON() : undefined,
      removedAt: departmentAuthority.removedAt?.isValid() ? departmentAuthority.removedAt.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createTime = res.body.createTime ? dayjs(res.body.createTime) : undefined;
      res.body.removedAt = res.body.removedAt ? dayjs(res.body.removedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((departmentAuthority: IDepartmentAuthority) => {
        departmentAuthority.createTime = departmentAuthority.createTime ? dayjs(departmentAuthority.createTime) : undefined;
        departmentAuthority.removedAt = departmentAuthority.removedAt ? dayjs(departmentAuthority.removedAt) : undefined;
      });
    }
    return res;
  }
}
