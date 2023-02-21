import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPosition, getPositionIdentifier } from '../position.model';

export type EntityResponseType = HttpResponse<IPosition>;
export type EntityArrayResponseType = HttpResponse<IPosition[]>;

@Injectable({ providedIn: 'root' })
export class PositionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/positions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(position: IPosition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(position);
    return this.http
      .post<IPosition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(position: IPosition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(position);
    return this.http
      .put<IPosition>(`${this.resourceUrl}/${getPositionIdentifier(position) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(position: IPosition, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(position);
    const copy = position;
    return this.http
      .put<IPosition>(this.resourceUrl + '/specified-fields', { position: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPosition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPosition[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addPositionToCollectionIfMissing(positionCollection: IPosition[], ...positionsToCheck: (IPosition | null | undefined)[]): IPosition[] {
    const positions: IPosition[] = positionsToCheck.filter(isPresent);
    if (positions.length > 0) {
      const positionCollectionIdentifiers = positionCollection.map(positionItem => getPositionIdentifier(positionItem)!);
      const positionsToAdd = positions.filter(positionItem => {
        const positionIdentifier = getPositionIdentifier(positionItem);
        if (positionIdentifier == null || positionCollectionIdentifiers.includes(positionIdentifier)) {
          return false;
        }
        positionCollectionIdentifiers.push(positionIdentifier);
        return true;
      });
      return [...positionsToAdd, ...positionCollection];
    }
    return positionCollection;
  }

  protected convertDateFromClient(position: IPosition): IPosition {
    return Object.assign({}, position, {
      removedAt: position.removedAt?.isValid() ? position.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((position: IPosition) => {
        position.removedAt = position.removedAt ? dayjs(position.removedAt) : undefined;
      });
    }
    return res;
  }
}
