import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, Observer } from 'rxjs';
import { filter, map } from 'rxjs/operators';
// import * as moment from 'moment';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

import { IResourceCategory } from '../resource-category.model';
import { ResourceCategoryService } from '../service/resource-category.service';
import { NzUploadFile } from 'ng-zorro-antd/upload';
import { NzMessageService } from 'ng-zorro-antd/message';
import { UPLOAD_IMAGE_URL, UPLOAD_FILE_URL } from 'app/app.constants';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-resource-category-update',
  templateUrl: './resource-category-update.component.html',
  styles: [
    `
      .avatar {
        width: 128px;
        height: 128px;
      }
      .upload-icon {
        font-size: 32px;
        color: #999;
      }
      .ant-upload-text {
        margin-top: 8px;
        color: #666;
      }
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class ResourceCategoryUpdateComponent implements OnInit {
  resourceCategory: IResourceCategory | null = null;
  isSaving = false;
  uploadFileUrl = UPLOAD_FILE_URL;
  uploadImageUrl = UPLOAD_IMAGE_URL;
  previewImage: string | undefined = '';
  previewVisible = false;
  showUploadList = {
    showPreviewIcon: true,
    showRemoveIcon: true,
    hidePreviewIconInNonImage: true,
  };

  resourceCategoriesCollectionNzTreeNodes: any[] = [];

  resourceCategoriesCollection: IResourceCategory[] = [];

  constructor(
    protected eventManager: EventManager,
    protected resourceCategoryService: ResourceCategoryService,
    protected activatedRoute: ActivatedRoute,
    private msg: NzMessageService
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ resourceCategory }) => {
      this.resourceCategory = resourceCategory;
    });
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
            this.resourceCategory?.parent?.id,
            'title',
            this.resourceCategory?.id
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
    if (this.resourceCategory?.id !== undefined) {
      this.subscribeToSaveResponse(this.resourceCategoryService.update(this.resourceCategory));
    } else {
      this.subscribeToSaveResponse(this.resourceCategoryService.create(this.resourceCategory!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResourceCategory>>): void {
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

  trackResourceCategoryById(index: number, item: IResourceCategory): number {
    return item.id!;
  }

  beforeUpload = (file: File) =>
    new Observable((observer: Observer<boolean>) => {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif';
      if (!isJPG) {
        this.msg.error('You can only upload JPG file!');
        observer.complete();
        return;
      }
      const isLt2M = file.size / 1024 / 1024 < 4;
      if (!isLt2M) {
        this.msg.error('Image must smaller than 4MB!');
        observer.complete();
        return;
      }
      // check height
      this.checkImageDimension(file).then(dimensionRes => {
        // observer.next(isJPG && isLt2M && dimensionRes);
        observer.next(isJPG && isLt2M);
        observer.complete();
      });
    });
  private getBase64(img: File, callback: (img: string) => void): void {
    const reader = new FileReader();
    reader.addEventListener('load', () => callback(reader.result!.toString()));
    reader.readAsDataURL(img);
  }
  private checkImageDimension(file: File): Promise<boolean> {
    return new Promise(resolve => {
      const img = new Image(); // create image
      img.src = window.URL.createObjectURL(file);
      img.onload = () => {
        const width = img.naturalWidth;
        const height = img.naturalHeight;
        window.URL.revokeObjectURL(img.src);
        resolve(width === height && width >= 3000);
      };
    });
  }
  handlePreview = (file: NzUploadFile) => {
    this.previewImage = file.url ?? file.thumbUrl;
    this.previewVisible = true;
  };
}
