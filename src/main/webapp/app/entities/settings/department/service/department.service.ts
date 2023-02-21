import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDepartment, getDepartmentIdentifier } from '../department.model';

export type EntityResponseType = HttpResponse<IDepartment>;
export type EntityArrayResponseType = HttpResponse<IDepartment[]>;

@Injectable({ providedIn: 'root' })
export class DepartmentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/departments');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(department: IDepartment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(department);
    return this.http
      .post<IDepartment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(department: IDepartment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(department);
    return this.http
      .put<IDepartment>(`${this.resourceUrl}/${getDepartmentIdentifier(department) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(department: IDepartment, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(department);
    const copy = department;
    return this.http
      .put<IDepartment>(this.resourceUrl + '/specified-fields', { department: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDepartment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepartment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  tree(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepartment[]>(this.resourceUrl + '/tree', { params: options, observe: 'response' })
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

  addDepartmentToCollectionIfMissing(
    departmentCollection: IDepartment[],
    ...departmentsToCheck: (IDepartment | null | undefined)[]
  ): IDepartment[] {
    const departments: IDepartment[] = departmentsToCheck.filter(isPresent);
    if (departments.length > 0) {
      const departmentCollectionIdentifiers = departmentCollection.map(departmentItem => getDepartmentIdentifier(departmentItem)!);
      const departmentsToAdd = departments.filter(departmentItem => {
        const departmentIdentifier = getDepartmentIdentifier(departmentItem);
        if (departmentIdentifier == null || departmentCollectionIdentifiers.includes(departmentIdentifier)) {
          return false;
        }
        departmentCollectionIdentifiers.push(departmentIdentifier);
        return true;
      });
      return [...departmentsToAdd, ...departmentCollection];
    }
    return departmentCollection;
  }

  protected convertDateFromClient(department: IDepartment): IDepartment {
    return Object.assign({}, department, {
      createTime: department.createTime?.isValid() ? department.createTime.toJSON() : undefined,
      removedAt: department.removedAt?.isValid() ? department.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((department: IDepartment) => {
        department.createTime = department.createTime ? dayjs(department.createTime) : undefined;
        department.removedAt = department.removedAt ? dayjs(department.removedAt) : undefined;
      });
    }
    return res;
  }
}
