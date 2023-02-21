import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
// import * as moment from 'moment';
// import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

import { IUploadFile } from '../upload-file.model';
import { UploadFileService } from '../service/upload-file.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IResourceCategory } from 'app/entities/files/resource-category/resource-category.model';
import { ResourceCategoryService } from 'app/entities/files/resource-category/service/resource-category.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-upload-file-update',
  templateUrl: './upload-file-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class UploadFileUpdateComponent implements OnInit {
  uploadFile: IUploadFile | null = null;
  isSaving = false;

  usersCollection: IUser[] = [];

  resourceCategoriesCollectionNzTreeNodes: any[] = [];

  resourceCategoriesCollection: IResourceCategory[] = [];

  constructor(
    protected eventManager: EventManager,
    protected uploadFileService: UploadFileService,
    protected userService: UserService,
    protected resourceCategoryService: ResourceCategoryService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ uploadFile }) => {
      this.uploadFile = uploadFile;

      // this.createAt = this.uploadFile.createAt != null ? this.uploadFile.createAt.format(DATE_TIME_FORMAT) : null;
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe(
        (res: IUser[] | null) => {
          this.usersCollection = res!;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.resourceCategoryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IResourceCategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<IResourceCategory[]>) => response.body)
      )
      .subscribe(
        (res: IResourceCategory[] | null) => {
          this.resourceCategoriesCollection = res!;
          // add by wang xin
          this.resourceCategoriesCollectionNzTreeNodes = toNzTreeNodeKeyId(
            this.resourceCategoriesCollection,
            this.uploadFile?.category?.id,
            'title'
          );
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    // this.uploadFile.createAt = this.createAt != null ? moment(this.createAt, DATE_TIME_FORMAT) : null;
    if (this.uploadFile?.id !== undefined) {
      this.subscribeToSaveResponse(this.uploadFileService.update(this.uploadFile));
    } else {
      this.subscribeToSaveResponse(this.uploadFileService.create(this.uploadFile!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUploadFile>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  protected onError(errorMessage: string): void {
    // this.jhiAlertService.error(errorMessage, null, null);
    console.log('error', errorMessage);
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackResourceCategoryById(index: number, item: IResourceCategory): number {
    return item.id!;
  }
}
