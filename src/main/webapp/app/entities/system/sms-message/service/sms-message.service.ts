import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISmsMessage, getSmsMessageIdentifier } from '../sms-message.model';

export type EntityResponseType = HttpResponse<ISmsMessage>;
export type EntityArrayResponseType = HttpResponse<ISmsMessage[]>;

@Injectable({ providedIn: 'root' })
export class SmsMessageService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/sms-messages');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(smsMessage: ISmsMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(smsMessage);
    return this.http
      .post<ISmsMessage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(smsMessage: ISmsMessage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(smsMessage);
    return this.http
      .put<ISmsMessage>(`${this.resourceUrl}/${getSmsMessageIdentifier(smsMessage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(smsMessage: ISmsMessage, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(smsMessage);
    const copy = smsMessage;
    return this.http
      .put<ISmsMessage>(this.resourceUrl + '/specified-fields', { smsMessage: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISmsMessage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISmsMessage[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addSmsMessageToCollectionIfMissing(
    smsMessageCollection: ISmsMessage[],
    ...smsMessagesToCheck: (ISmsMessage | null | undefined)[]
  ): ISmsMessage[] {
    const smsMessages: ISmsMessage[] = smsMessagesToCheck.filter(isPresent);
    if (smsMessages.length > 0) {
      const smsMessageCollectionIdentifiers = smsMessageCollection.map(smsMessageItem => getSmsMessageIdentifier(smsMessageItem)!);
      const smsMessagesToAdd = smsMessages.filter(smsMessageItem => {
        const smsMessageIdentifier = getSmsMessageIdentifier(smsMessageItem);
        if (smsMessageIdentifier == null || smsMessageCollectionIdentifiers.includes(smsMessageIdentifier)) {
          return false;
        }
        smsMessageCollectionIdentifiers.push(smsMessageIdentifier);
        return true;
      });
      return [...smsMessagesToAdd, ...smsMessageCollection];
    }
    return smsMessageCollection;
  }

  protected convertDateFromClient(smsMessage: ISmsMessage): ISmsMessage {
    return Object.assign({}, smsMessage, {
      sendTime: smsMessage.sendTime?.isValid() ? smsMessage.sendTime.toJSON() : undefined,
      removedAt: smsMessage.removedAt?.isValid() ? smsMessage.removedAt.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.sendTime = res.body.sendTime ? dayjs(res.body.sendTime) : undefined;
      res.body.removedAt = res.body.removedAt ? dayjs(res.body.removedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((smsMessage: ISmsMessage) => {
        smsMessage.sendTime = smsMessage.sendTime ? dayjs(smsMessage.sendTime) : undefined;
        smsMessage.removedAt = smsMessage.removedAt ? dayjs(smsMessage.removedAt) : undefined;
      });
    }
    return res;
  }
}
