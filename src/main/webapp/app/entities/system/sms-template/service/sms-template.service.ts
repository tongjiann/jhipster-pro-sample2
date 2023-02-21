import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISmsTemplate, getSmsTemplateIdentifier } from '../sms-template.model';

export type EntityResponseType = HttpResponse<ISmsTemplate>;
export type EntityArrayResponseType = HttpResponse<ISmsTemplate[]>;

@Injectable({ providedIn: 'root' })
export class SmsTemplateService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/sms-templates');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(smsTemplate: ISmsTemplate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(smsTemplate);
    return this.http
      .post<ISmsTemplate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(smsTemplate: ISmsTemplate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(smsTemplate);
    return this.http
      .put<ISmsTemplate>(`${this.resourceUrl}/${getSmsTemplateIdentifier(smsTemplate) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(smsTemplate: ISmsTemplate, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(smsTemplate);
    const copy = smsTemplate;
    return this.http
      .put<ISmsTemplate>(this.resourceUrl + '/specified-fields', { smsTemplate: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISmsTemplate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISmsTemplate[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addSmsTemplateToCollectionIfMissing(
    smsTemplateCollection: ISmsTemplate[],
    ...smsTemplatesToCheck: (ISmsTemplate | null | undefined)[]
  ): ISmsTemplate[] {
    const smsTemplates: ISmsTemplate[] = smsTemplatesToCheck.filter(isPresent);
    if (smsTemplates.length > 0) {
      const smsTemplateCollectionIdentifiers = smsTemplateCollection.map(smsTemplateItem => getSmsTemplateIdentifier(smsTemplateItem)!);
      const smsTemplatesToAdd = smsTemplates.filter(smsTemplateItem => {
        const smsTemplateIdentifier = getSmsTemplateIdentifier(smsTemplateItem);
        if (smsTemplateIdentifier == null || smsTemplateCollectionIdentifiers.includes(smsTemplateIdentifier)) {
          return false;
        }
        smsTemplateCollectionIdentifiers.push(smsTemplateIdentifier);
        return true;
      });
      return [...smsTemplatesToAdd, ...smsTemplateCollection];
    }
    return smsTemplateCollection;
  }

  protected convertDateFromClient(smsTemplate: ISmsTemplate): ISmsTemplate {
    return Object.assign({}, smsTemplate, {
      removedAt: smsTemplate.removedAt?.isValid() ? smsTemplate.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((smsTemplate: ISmsTemplate) => {
        smsTemplate.removedAt = smsTemplate.removedAt ? dayjs(smsTemplate.removedAt) : undefined;
      });
    }
    return res;
  }
}
