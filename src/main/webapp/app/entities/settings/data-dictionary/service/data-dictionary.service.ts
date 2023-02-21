import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDataDictionary, getDataDictionaryIdentifier } from '../data-dictionary.model';

export type EntityResponseType = HttpResponse<IDataDictionary>;
export type EntityArrayResponseType = HttpResponse<IDataDictionary[]>;

@Injectable({ providedIn: 'root' })
export class DataDictionaryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/data-dictionaries');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(dataDictionary: IDataDictionary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataDictionary);
    return this.http
      .post<IDataDictionary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dataDictionary: IDataDictionary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataDictionary);
    return this.http
      .put<IDataDictionary>(`${this.resourceUrl}/${getDataDictionaryIdentifier(dataDictionary) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(dataDictionary: IDataDictionary, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(dataDictionary);
    const copy = dataDictionary;
    return this.http
      .put<IDataDictionary>(this.resourceUrl + '/specified-fields', { dataDictionary: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDataDictionary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDataDictionary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  tree(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDataDictionary[]>(this.resourceUrl + '/tree', { params: options, observe: 'response' })
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

  addDataDictionaryToCollectionIfMissing(
    dataDictionaryCollection: IDataDictionary[],
    ...dataDictionariesToCheck: (IDataDictionary | null | undefined)[]
  ): IDataDictionary[] {
    const dataDictionaries: IDataDictionary[] = dataDictionariesToCheck.filter(isPresent);
    if (dataDictionaries.length > 0) {
      const dataDictionaryCollectionIdentifiers = dataDictionaryCollection.map(
        dataDictionaryItem => getDataDictionaryIdentifier(dataDictionaryItem)!
      );
      const dataDictionariesToAdd = dataDictionaries.filter(dataDictionaryItem => {
        const dataDictionaryIdentifier = getDataDictionaryIdentifier(dataDictionaryItem);
        if (dataDictionaryIdentifier == null || dataDictionaryCollectionIdentifiers.includes(dataDictionaryIdentifier)) {
          return false;
        }
        dataDictionaryCollectionIdentifiers.push(dataDictionaryIdentifier);
        return true;
      });
      return [...dataDictionariesToAdd, ...dataDictionaryCollection];
    }
    return dataDictionaryCollection;
  }

  protected convertDateFromClient(dataDictionary: IDataDictionary): IDataDictionary {
    return Object.assign({}, dataDictionary, {
      removedAt: dataDictionary.removedAt?.isValid() ? dataDictionary.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((dataDictionary: IDataDictionary) => {
        dataDictionary.removedAt = dataDictionary.removedAt ? dayjs(dataDictionary.removedAt) : undefined;
      });
    }
    return res;
  }
}
