import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUploadImage, getUploadImageIdentifier } from '../upload-image.model';

export type EntityResponseType = HttpResponse<IUploadImage>;
export type EntityArrayResponseType = HttpResponse<IUploadImage[]>;

@Injectable({ providedIn: 'root' })
export class UploadImageService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/upload-images');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(uploadImage: IUploadImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(uploadImage);
    return this.http
      .post<IUploadImage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(uploadImage: IUploadImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(uploadImage);
    return this.http
      .put<IUploadImage>(`${this.resourceUrl}/${getUploadImageIdentifier(uploadImage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(uploadImage: IUploadImage, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(uploadImage);
    const copy = uploadImage;
    return this.http
      .put<IUploadImage>(this.resourceUrl + '/specified-fields', { uploadImage: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUploadImage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUploadImage[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addUploadImageToCollectionIfMissing(
    uploadImageCollection: IUploadImage[],
    ...uploadImagesToCheck: (IUploadImage | null | undefined)[]
  ): IUploadImage[] {
    const uploadImages: IUploadImage[] = uploadImagesToCheck.filter(isPresent);
    if (uploadImages.length > 0) {
      const uploadImageCollectionIdentifiers = uploadImageCollection.map(uploadImageItem => getUploadImageIdentifier(uploadImageItem)!);
      const uploadImagesToAdd = uploadImages.filter(uploadImageItem => {
        const uploadImageIdentifier = getUploadImageIdentifier(uploadImageItem);
        if (uploadImageIdentifier == null || uploadImageCollectionIdentifiers.includes(uploadImageIdentifier)) {
          return false;
        }
        uploadImageCollectionIdentifiers.push(uploadImageIdentifier);
        return true;
      });
      return [...uploadImagesToAdd, ...uploadImageCollection];
    }
    return uploadImageCollection;
  }

  protected convertDateFromClient(uploadImage: IUploadImage): IUploadImage {
    return Object.assign({}, uploadImage, {
      createAt: uploadImage.createAt?.isValid() ? uploadImage.createAt.toJSON() : undefined,
      removedAt: uploadImage.removedAt?.isValid() ? uploadImage.removedAt.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createAt = res.body.createAt ? dayjs(res.body.createAt) : undefined;
      res.body.removedAt = res.body.removedAt ? dayjs(res.body.removedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((uploadImage: IUploadImage) => {
        uploadImage.createAt = uploadImage.createAt ? dayjs(uploadImage.createAt) : undefined;
        uploadImage.removedAt = uploadImage.removedAt ? dayjs(uploadImage.removedAt) : undefined;
      });
    }
    return res;
  }
}
