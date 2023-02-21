import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDataPermissionRule, getDataPermissionRuleIdentifier } from '../data-permission-rule.model';

export type EntityResponseType = HttpResponse<IDataPermissionRule>;
export type EntityArrayResponseType = HttpResponse<IDataPermissionRule[]>;

@Injectable({ providedIn: 'root' })
export class DataPermissionRuleService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/data-permission-rules');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(dataPermissionRule: IDataPermissionRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataPermissionRule);
    return this.http
      .post<IDataPermissionRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dataPermissionRule: IDataPermissionRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataPermissionRule);
    return this.http
      .put<IDataPermissionRule>(`${this.resourceUrl}/${getDataPermissionRuleIdentifier(dataPermissionRule) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(dataPermissionRule: IDataPermissionRule, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(dataPermissionRule);
    const copy = dataPermissionRule;
    return this.http
      .put<IDataPermissionRule>(
        this.resourceUrl + '/specified-fields',
        { dataPermissionRule: copy, specifiedFields },
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDataPermissionRule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDataPermissionRule[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addDataPermissionRuleToCollectionIfMissing(
    dataPermissionRuleCollection: IDataPermissionRule[],
    ...dataPermissionRulesToCheck: (IDataPermissionRule | null | undefined)[]
  ): IDataPermissionRule[] {
    const dataPermissionRules: IDataPermissionRule[] = dataPermissionRulesToCheck.filter(isPresent);
    if (dataPermissionRules.length > 0) {
      const dataPermissionRuleCollectionIdentifiers = dataPermissionRuleCollection.map(
        dataPermissionRuleItem => getDataPermissionRuleIdentifier(dataPermissionRuleItem)!
      );
      const dataPermissionRulesToAdd = dataPermissionRules.filter(dataPermissionRuleItem => {
        const dataPermissionRuleIdentifier = getDataPermissionRuleIdentifier(dataPermissionRuleItem);
        if (dataPermissionRuleIdentifier == null || dataPermissionRuleCollectionIdentifiers.includes(dataPermissionRuleIdentifier)) {
          return false;
        }
        dataPermissionRuleCollectionIdentifiers.push(dataPermissionRuleIdentifier);
        return true;
      });
      return [...dataPermissionRulesToAdd, ...dataPermissionRuleCollection];
    }
    return dataPermissionRuleCollection;
  }

  protected convertDateFromClient(dataPermissionRule: IDataPermissionRule): IDataPermissionRule {
    return Object.assign({}, dataPermissionRule, {
      removedAt: dataPermissionRule.removedAt?.isValid() ? dataPermissionRule.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((dataPermissionRule: IDataPermissionRule) => {
        dataPermissionRule.removedAt = dataPermissionRule.removedAt ? dayjs(dataPermissionRule.removedAt) : undefined;
      });
    }
    return res;
  }
}
