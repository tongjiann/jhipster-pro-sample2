import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISysFillRule, getSysFillRuleIdentifier } from '../sys-fill-rule.model';

export type EntityResponseType = HttpResponse<ISysFillRule>;
export type EntityArrayResponseType = HttpResponse<ISysFillRule[]>;

@Injectable({ providedIn: 'root' })
export class SysFillRuleService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/sys-fill-rules');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(sysFillRule: ISysFillRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sysFillRule);
    return this.http
      .post<ISysFillRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sysFillRule: ISysFillRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sysFillRule);
    return this.http
      .put<ISysFillRule>(`${this.resourceUrl}/${getSysFillRuleIdentifier(sysFillRule) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(sysFillRule: ISysFillRule, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(sysFillRule);
    const copy = sysFillRule;
    return this.http
      .put<ISysFillRule>(this.resourceUrl + '/specified-fields', { sysFillRule: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISysFillRule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISysFillRule[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addSysFillRuleToCollectionIfMissing(
    sysFillRuleCollection: ISysFillRule[],
    ...sysFillRulesToCheck: (ISysFillRule | null | undefined)[]
  ): ISysFillRule[] {
    const sysFillRules: ISysFillRule[] = sysFillRulesToCheck.filter(isPresent);
    if (sysFillRules.length > 0) {
      const sysFillRuleCollectionIdentifiers = sysFillRuleCollection.map(sysFillRuleItem => getSysFillRuleIdentifier(sysFillRuleItem)!);
      const sysFillRulesToAdd = sysFillRules.filter(sysFillRuleItem => {
        const sysFillRuleIdentifier = getSysFillRuleIdentifier(sysFillRuleItem);
        if (sysFillRuleIdentifier == null || sysFillRuleCollectionIdentifiers.includes(sysFillRuleIdentifier)) {
          return false;
        }
        sysFillRuleCollectionIdentifiers.push(sysFillRuleIdentifier);
        return true;
      });
      return [...sysFillRulesToAdd, ...sysFillRuleCollection];
    }
    return sysFillRuleCollection;
  }

  protected convertDateFromClient(sysFillRule: ISysFillRule): ISysFillRule {
    return Object.assign({}, sysFillRule, {
      removedAt: sysFillRule.removedAt?.isValid() ? sysFillRule.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((sysFillRule: ISysFillRule) => {
        sysFillRule.removedAt = sysFillRule.removedAt ? dayjs(sysFillRule.removedAt) : undefined;
      });
    }
    return res;
  }
}
