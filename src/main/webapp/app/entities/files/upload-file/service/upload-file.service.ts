import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUploadFile, getUploadFileIdentifier } from '../upload-file.model';

export type EntityResponseType = HttpResponse<IUploadFile>;
export type EntityArrayResponseType = HttpResponse<IUploadFile[]>;

@Injectable({ providedIn: 'root' })
export class UploadFileService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/upload-files');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(uploadFile: IUploadFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(uploadFile);
    return this.http
      .post<IUploadFile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(uploadFile: IUploadFile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(uploadFile);
    return this.http
      .put<IUploadFile>(`${this.resourceUrl}/${getUploadFileIdentifier(uploadFile) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateBySpecifiedFields(uploadFile: IUploadFile, specifiedFields: string[]): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(uploadFile);
    const copy = uploadFile;
    return this.http
      .put<IUploadFile>(this.resourceUrl + '/specified-fields', { uploadFile: copy, specifiedFields }, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUploadFile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUploadFile[]>(this.resourceUrl, { params: options, observe: 'response' })
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

  addUploadFileToCollectionIfMissing(
    uploadFileCollection: IUploadFile[],
    ...uploadFilesToCheck: (IUploadFile | null | undefined)[]
  ): IUploadFile[] {
    const uploadFiles: IUploadFile[] = uploadFilesToCheck.filter(isPresent);
    if (uploadFiles.length > 0) {
      const uploadFileCollectionIdentifiers = uploadFileCollection.map(uploadFileItem => getUploadFileIdentifier(uploadFileItem)!);
      const uploadFilesToAdd = uploadFiles.filter(uploadFileItem => {
        const uploadFileIdentifier = getUploadFileIdentifier(uploadFileItem);
        if (uploadFileIdentifier == null || uploadFileCollectionIdentifiers.includes(uploadFileIdentifier)) {
          return false;
        }
        uploadFileCollectionIdentifiers.push(uploadFileIdentifier);
        return true;
      });
      return [...uploadFilesToAdd, ...uploadFileCollection];
    }
    return uploadFileCollection;
  }

  protected convertDateFromClient(uploadFile: IUploadFile): IUploadFile {
    return Object.assign({}, uploadFile, {
      createAt: uploadFile.createAt?.isValid() ? uploadFile.createAt.toJSON() : undefined,
      removedAt: uploadFile.removedAt?.isValid() ? uploadFile.removedAt.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((uploadFile: IUploadFile) => {
        uploadFile.createAt = uploadFile.createAt ? dayjs(uploadFile.createAt) : undefined;
        uploadFile.removedAt = uploadFile.removedAt ? dayjs(uploadFile.removedAt) : undefined;
      });
    }
    return res;
  }
}
