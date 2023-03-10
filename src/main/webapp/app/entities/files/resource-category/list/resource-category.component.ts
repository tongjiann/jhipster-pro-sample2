import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
// import * as moment from 'moment';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';

import { IResourceCategory, ResourceCategory } from '../resource-category.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ResourceCategoryService } from '../service/resource-category.service';
import { Observable, Observer, Subscription } from 'rxjs';
// import { filter, map } from 'rxjs/operators';
import { NzUploadFile } from 'ng-zorro-antd/upload';
import { UPLOAD_IMAGE_URL, UPLOAD_FILE_URL } from 'app/app.constants';
import { TranslateService } from '@ngx-translate/core';
import { ICommonTable } from 'app/entities/modelConfig/common-table/common-table.model';
import { CommonTableService } from 'app/entities/modelConfig/common-table/service/common-table.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';
import { calculateFieldWidth, calculateRelationshipWidth, calculateTableWidth } from 'app/shared/util/table-util';

@Component({
  selector: 'jhi-resource-category',
  templateUrl: './resource-category.component.html',
  styles: [
    `
      .search-box {
        padding: 8px;
      }

      .search-box > div {
        width: 188px;
        margin-bottom: 8px;
        display: block;
      }

      .search-box > input {
        width: 188px;
        margin-bottom: 8px;
        display: block;
      }

      .search-box > div > input {
        width: 84px;
        margin-bottom: 8px;
      }

      .search-box button {
        width: 90px;
      }

      .search-button {
        margin-right: 8px;
      }
    `,
  ],
})
export class ResourceCategoryComponent implements OnInit {
  sortName: string | null = null;
  sortValue: string | null = null;
  // ??????
  // value :"descend" or "ascend" or null
  mapOfSort: { [key: string]: any } = {};
  // ??????
  searchValue = '';
  dateRange = [];
  listOfFilterStatus = [];
  listOfFilterStatusValue: string[] = [];
  resourceCategoryCommonTableData: ICommonTable | null = null; // ??????????????????
  mapOfFilter: { [key: string]: any } = {};
  // ??????????????????????????????
  specifiedFields = ['title', 'createAt', 'intro', 'pageViews', 'status', 'top', 'hot', 'columnId', 'picId', 'userId'];
  // ????????????????????????
  editStatus: { [key: string]: any } = {};
  // ????????????????????????????????????
  omitFields = ['id'];
  // ???????????????????????????
  mapOfCheckedId: { [key: string]: boolean } = {};
  // ??????????????????????????????
  isAllDisplayDataChecked = false;
  // ??????????????????????????????????????????
  isIndeterminate = false;
  // ?????????????????????
  numberOfChecked = 0;
  currentAccount: any;
  resourceCategories?: IResourceCategory[];
  isLoading = false;
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
  error: any;
  success: any;
  routeData: any;
  links: any;
  totalItems = 0;
  queryCount: any;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  previousPage: any;
  // treetable 1.
  expandDataCache: any = {};

  resourceCategoriesCollectionNzTreeNodes: any[] = [];

  resourceCategoriesCollection: IResourceCategory[] = [];
  constructor(
    protected resourceCategoryService: ResourceCategoryService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    private translateService: TranslateService,
    protected router: Router,
    protected modalService: NzModalService,
    private msg: NzMessageService,
    protected commonTableService: CommonTableService
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    /* this.routeData = this.activatedRoute?.data?.subscribe((data) => {
            this.page = data?.pagingParams?.page ?? 1;
            this.previousPage = data?.pagingParams?.page ?? 0;
            this.ascending = data?.pagingParams?.ascending ?? true;
            this.predicate = data?.pagingParams?.predicate ?? 'id';
            this.resourceCategoryCommonTableData = data.commonTableData[0];
        }); */
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
    const requestOption: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      filter: this.getFilter(),
      sort: this.getSort(),
    };

    this.resourceCategoryService.tree(requestOption).subscribe(
      (res: HttpResponse<IResourceCategory[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers, pageToLoad, false);
      },
      error => {
        this.isLoading = false;
        this.onError(error.message);
      }
    );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });

    // ??????enum
  }

  trackResourceCategoryById(index: number, item: IResourceCategory) {
    return item.id;
  }
  trackId(index: number, item: IResourceCategory) {
    return item.id;
  }

  deleteConfirm(id: number | undefined): void {
    if (!id) {
      return;
    }
    this.modalService.confirm({
      nzTitle: '???????????????id???' + id + '??????????',
      nzOkText: '??????',
      nzOkType: 'danger',
      nzOnOk: () => this.delete(id),
      nzCancelText: '??????',
    });
  }

  deleteByIdsConfirm(): void {
    const ids: number[] = [];
    Object.keys(this.mapOfCheckedId).forEach(key => {
      if (this.mapOfCheckedId[key]) {
        ids.push(Number(key));
      }
    });
    this.modalService.confirm({
      nzTitle: '???????????????id???' + ids.join(';') + '??????????',
      nzOkText: '??????',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteByIds(ids),
      nzCancelText: '??????',
    });
  }

  delete(id: number | undefined): void {
    if (!id) {
      return;
    }
    this.resourceCategoryService.delete(id).subscribe(
      (res: HttpResponse<any>) => {
        this.msg.success('???????????????');
        this.loadPage();
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  deleteByIds(ids: number[]): void {
    this.resourceCategoryService.deleteByIds(ids).subscribe(
      (res: HttpResponse<any>) => {
        this.msg.success('?????????????????????');
        this.loadPage();
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  protected paginateResourceCategories(data: IResourceCategory[], headers: HttpHeaders) {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.queryCount = this.totalItems;
    // ???????????????????????????????????????????????????????????????
    data.forEach(resourceCategory => {
      this.editStatus[resourceCategory.id!] = {
        edit: false,
      };
      this.mapOfCheckedId[resourceCategory.id!] = false;
    });
    this.resourceCategories = data;

    // treetable 5.
    this.resourceCategories.forEach(item => {
      this.expandDataCache[item.id!] = this.convertTreeToList(item);
    });
  }

  protected onError(errorMessage: string) {
    this.page = 0;
    this.msg.error(errorMessage);
  }

  // treetable 2.
  collapse(array: IResourceCategory[], data: IResourceCategory, $event: boolean): void {
    if ($event === false) {
      if (data.children) {
        data.children.forEach(d => {
          const target = array.find(a => a.id === d.id);
          target!.expand = false;
          this.collapse(array, target!, false);
        });
      } else {
        return;
      }
    }
  }

  // treetable 3.
  convertTreeToList(root: any): IResourceCategory[] {
    const stack = [];
    const tempArray: any[] = [];
    const hashMap = {};
    stack.push({ ...root, nzAddLevel: 0, expand: false });
    while (stack.length !== 0) {
      const node = stack.pop();
      this.visitNode(node, hashMap, tempArray);
      if (node.children) {
        for (let i = node.children.length - 1; i >= 0; i--) {
          stack.push({ ...node.children[i], nzAddLevel: node.nzAddLevel + 1, expand: false, parent: node });
        }
      }
    }
    return tempArray;
  }
  // treetable  4.
  visitNode(node: IResourceCategory, hashMap: any, array: IResourceCategory[]): void {
    if (!hashMap[node.id!]) {
      hashMap[node.id!] = true;
      array.push(node);
    }
  }
  saveEdit(resourceCategory: IResourceCategory) {
    this.isSaving = true;
    if (resourceCategory.id !== undefined) {
      if (this.omitFields.length > 1) {
        this.subscribeToSaveResponse(this.resourceCategoryService.updateBySpecifiedFields(resourceCategory, this.omitFields));
      } else {
        this.subscribeToSaveResponse(this.resourceCategoryService.update(resourceCategory));
      }
    } else {
      this.subscribeToSaveResponse(this.resourceCategoryService.create(resourceCategory));
    }
    this.editStatus[resourceCategory.id!].edit = false;
  }
  cancelEdit(id: number | undefined): void {
    if (id) {
      this.editStatus[id].edit = false;
      this.loadPage();
    }
  }
  // ?????? ???????????????????????????
  newResourceCategory() {
    const resourceCategory = new ResourceCategory();
    resourceCategory.id = -1;
    this.resourceCategories?.push(resourceCategory);
    this.editStatus[resourceCategory.id].edit = true;
  }
  // ?????????????????????
  startEdit(resourceCategory: IResourceCategory): void {
    this.editStatus[resourceCategory.id!].edit = true;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResourceCategory>>) {
    result.subscribe(
      (res: HttpResponse<IResourceCategory>) => this.onSaveSuccess(),
      (res: HttpErrorResponse) => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    // this.isSaving = false;
    this.loadPage();
  }

  protected onSaveError(): void {
    // this.isSaving = false;
  }
  // ?????????????????????
  refreshStatus(): void {
    this.isAllDisplayDataChecked = this.resourceCategories?.every(item => this.mapOfCheckedId[item.id!])!;
    this.isIndeterminate = this.resourceCategories?.some(item => this.mapOfCheckedId[item.id!])! && !this.isAllDisplayDataChecked;
    this.numberOfChecked = this.resourceCategories?.filter(item => this.mapOfCheckedId[item.id!]).length!;
  }

  checkAll(value: boolean): void {
    this.resourceCategories?.forEach(item => (this.mapOfCheckedId[item.id!] = value));
    this.refreshStatus();
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IResourceCategory[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/resource-category'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.resourceCategories = data ?? [];
    this.ngbPaginationPage = this.page;
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
  sort(sortName: string, value: string | null): void {
    const softKeys = Object.keys(this.mapOfSort);
    if (softKeys.length === 2 && softKeys[0] === 'id' && sortName !== 'id') {
      delete this.mapOfSort['id'];
    }
    if (Object.keys(this.mapOfSort).includes(sortName)) {
      if (value === null) {
        delete this.mapOfSort[sortName];
      } else {
        this.mapOfSort[sortName] = value;
      }
    }
    this.loadPage();
  }
  search(): void {}

  reset(): void {
    this.searchValue = '';
    this.search();
  }

  getSort() {
    const result = [];
    if (Object.keys(this.mapOfSort).length === 0) {
      this.mapOfSort.id = 'ascend';
    }
    for (const key in this.mapOfSort) {
      if (this.mapOfSort[key]) {
        const direction = this.mapOfSort[key] === 'descend' ? 'DESC' : 'ASC';
        result.push(key + ',' + direction);
      }
    }
    return result;
  }
  filter(fieldName: string, filterValue: string[]) {
    this.mapOfFilter[fieldName].value = filterValue;
    this.loadPage();
  }
  getFilter() {
    const result: { [key: string]: any } = {};
    if (this.searchValue) {
      result['jhiCommonSearchKeywords'] = this.searchValue;
      return result;
    }
    Object.keys(this.mapOfFilter).forEach(key => {
      const filterResult: any[] = [];
      if (this.mapOfFilter[key].type === 'Enum') {
        this.mapOfFilter[key].value.forEach((value: any) => {
          filterResult.push(value);
        });
        result[key + '.in'] = filterResult;
      }
      if (['one-to-one', 'many-to-many', 'many-to-one', 'one-to-many'].includes(this.mapOfFilter[key].type)) {
        this.mapOfFilter[key].value.forEach((value: any) => {
          filterResult.push(value);
        });
        result[key + 'Id.in'] = filterResult;
      }
    });
    return result;
  }

  showField(fieldName: string): boolean {
    const fields = this.resourceCategoryCommonTableData?.commonTableFields;
    if (!fields) {
      return true;
    }
    return fields?.some(field => field.entityFieldName === fieldName && !field.hideInList);
  }

  showRelationship(relationshipName: string): boolean {
    const relationships = this.resourceCategoryCommonTableData?.relationships;
    if (!relationships) {
      return true;
    }
    return relationships?.some(relationship => relationship.relationshipName === relationshipName && !relationship.hideInList);
  }

  fieldWidth(fieldName: string): string {
    if (!this.resourceCategoryCommonTableData) {
      return '100px';
    }
    return calculateFieldWidth(this.resourceCategoryCommonTableData, fieldName);
  }

  relationshipWidth(relationshipName: string): string {
    if (!this.resourceCategoryCommonTableData) {
      return '100px';
    }
    return calculateRelationshipWidth(this.resourceCategoryCommonTableData, relationshipName);
  }

  tableWidth(): string {
    return calculateTableWidth(this.resourceCategoryCommonTableData!);
  }
}
