import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResourceCategory, getResourceCategoryIdentifier } from '../resource-category.model';

export type EntityResponseType = HttpResponse<IResourceCategory>;
export type EntityArrayResponseType = HttpResponse<IResourceCategory[]>;

@Injectable({ providedIn: 'root' })
export class ResourceCategoryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/resource-categories');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(resourceCategory: IResourceCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resourceCategory);
    return this.http
      .post<IResourceCategory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(resourceCategory: IResourceCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(resourceCategory);
    return this.http
      .put<IResourceCategory>(`${this.resourceUrl}/${getResourceCategoryIdentifier(resourceCategory) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(resourceCategory: IResourceCategory, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(resourceCategory);
    const copy = resourceCategory;
    return this.http
      .put<IResourceCategory>(this.resourceUrl + '/specified-fields', { resourceCategory: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IResourceCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IResourceCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }
  tree(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IResourceCategory[]>(this.resourceUrl + '/tree', { params: options, observe: 'response' })
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

  addResourceCategoryToCollectionIfMissing(
    resourceCategoryCollection: IResourceCategory[],
    ...resourceCategoriesToCheck: (IResourceCategory | null | undefined)[]
  ): IResourceCategory[] {
    const resourceCategories: IResourceCategory[] = resourceCategoriesToCheck.filter(isPresent);
    if (resourceCategories.length > 0) {
      const resourceCategoryCollectionIdentifiers = resourceCategoryCollection.map(
        resourceCategoryItem => getResourceCategoryIdentifier(resourceCategoryItem)!
      );
      const resourceCategoriesToAdd = resourceCategories.filter(resourceCategoryItem => {
        const resourceCategoryIdentifier = getResourceCategoryIdentifier(resourceCategoryItem);
        if (resourceCategoryIdentifier == null || resourceCategoryCollectionIdentifiers.includes(resourceCategoryIdentifier)) {
          return false;
        }
        resourceCategoryCollectionIdentifiers.push(resourceCategoryIdentifier);
        return true;
      });
      return [...resourceCategoriesToAdd, ...resourceCategoryCollection];
    }
    return resourceCategoryCollection;
  }

  protected convertDateFromClient(resourceCategory: IResourceCategory): IResourceCategory {
    return Object.assign({}, resourceCategory, {
      removedAt: resourceCategory.removedAt?.isValid() ? resourceCategory.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((resourceCategory: IResourceCategory) => {
        resourceCategory.removedAt = resourceCategory.removedAt ? dayjs(resourceCategory.removedAt) : undefined;
      });
    }
    return res;
  }
}
