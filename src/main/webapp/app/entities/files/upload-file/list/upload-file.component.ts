import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
// import * as moment from 'moment';
// import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';

import { IUploadFile, UploadFile } from '../upload-file.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { UploadFileService } from '../service/upload-file.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IResourceCategory } from 'app/entities/files/resource-category/resource-category.model';
import { ResourceCategoryService } from 'app/entities/files/resource-category/service/resource-category.service';
import { Observable, Subscription } from 'rxjs';
// import { filter, map } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import { ICommonTable } from 'app/entities/modelConfig/common-table/common-table.model';
import { CommonTableService } from 'app/entities/modelConfig/common-table/service/common-table.service';
import { calculateFieldWidth, calculateRelationshipWidth, calculateTableWidth } from 'app/shared/util/table-util';

@Component({
  selector: 'jhi-upload-file',
  templateUrl: './upload-file.component.html',
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
export class UploadFileComponent implements OnInit {
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
  uploadFileCommonTableData: ICommonTable | null = null; // ??????????????????
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
  uploadFiles?: IUploadFile[];
  isLoading = false;
  isSaving = false;
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

  usersCollection: IUser[] = [];

  resourceCategoriesCollectionNzTreeNodes: any[] = [];

  resourceCategoriesCollection: IResourceCategory[] = [];
  constructor(
    protected uploadFileService: UploadFileService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    private translateService: TranslateService,
    protected router: Router,
    protected userService: UserService,
    protected resourceCategoryService: ResourceCategoryService,
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
            this.uploadFileCommonTableData = data.commonTableData[0];
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

    this.uploadFileService.query(requestOption).subscribe(
      (res: HttpResponse<IUploadFile[]>) => {
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackResourceCategoryById(index: number, item: IResourceCategory) {
    return item.id;
  }
  trackId(index: number, item: IUploadFile) {
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
    this.uploadFileService.delete(id).subscribe(
      (res: HttpResponse<any>) => {
        this.msg.success('???????????????');
        this.loadPage();
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  deleteByIds(ids: number[]): void {
    this.uploadFileService.deleteByIds(ids).subscribe(
      (res: HttpResponse<any>) => {
        this.msg.success('?????????????????????');
        this.loadPage();
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  protected paginateUploadFiles(data: IUploadFile[], headers: HttpHeaders) {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.queryCount = this.totalItems;
    // ???????????????????????????????????????????????????????????????
    data.forEach(uploadFile => {
      this.editStatus[uploadFile.id!] = {
        edit: false,
      };
      this.mapOfCheckedId[uploadFile.id!] = false;
    });
    this.uploadFiles = data;
  }

  protected onError(errorMessage: string) {
    this.page = 0;
    this.msg.error(errorMessage);
  }

  saveEdit(uploadFile: IUploadFile) {
    this.isSaving = true;
    if (uploadFile.id !== undefined) {
      if (this.omitFields.length > 1) {
        this.subscribeToSaveResponse(this.uploadFileService.updateBySpecifiedFields(uploadFile, this.omitFields));
      } else {
        this.subscribeToSaveResponse(this.uploadFileService.update(uploadFile));
      }
    } else {
      this.subscribeToSaveResponse(this.uploadFileService.create(uploadFile));
    }
    this.editStatus[uploadFile.id!].edit = false;
  }
  cancelEdit(id: number | undefined): void {
    if (id) {
      this.editStatus[id].edit = false;
      this.loadPage();
    }
  }
  // ?????? ???????????????????????????
  newUploadFile() {
    const uploadFile = new UploadFile();
    uploadFile.id = -1;
    this.uploadFiles?.push(uploadFile);
    this.editStatus[uploadFile.id].edit = true;
  }
  // ?????????????????????
  startEdit(uploadFile: IUploadFile): void {
    this.editStatus[uploadFile.id!].edit = true;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUploadFile>>) {
    result.subscribe(
      (res: HttpResponse<IUploadFile>) => this.onSaveSuccess(),
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
    this.isAllDisplayDataChecked = this.uploadFiles?.every(item => this.mapOfCheckedId[item.id!])!;
    this.isIndeterminate = this.uploadFiles?.some(item => this.mapOfCheckedId[item.id!])! && !this.isAllDisplayDataChecked;
    this.numberOfChecked = this.uploadFiles?.filter(item => this.mapOfCheckedId[item.id!]).length!;
  }

  checkAll(value: boolean): void {
    this.uploadFiles?.forEach(item => (this.mapOfCheckedId[item.id!] = value));
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

  protected onSuccess(data: IUploadFile[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/upload-file'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.uploadFiles = data ?? [];
    this.ngbPaginationPage = this.page;
  }

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
    const fields = this.uploadFileCommonTableData?.commonTableFields;
    if (!fields) {
      return true;
    }
    return fields?.some(field => field.entityFieldName === fieldName && !field.hideInList);
  }

  showRelationship(relationshipName: string): boolean {
    const relationships = this.uploadFileCommonTableData?.relationships;
    if (!relationships) {
      return true;
    }
    return relationships?.some(relationship => relationship.relationshipName === relationshipName && !relationship.hideInList);
  }

  fieldWidth(fieldName: string): string {
    if (!this.uploadFileCommonTableData) {
      return '100px';
    }
    return calculateFieldWidth(this.uploadFileCommonTableData, fieldName);
  }

  relationshipWidth(relationshipName: string): string {
    if (!this.uploadFileCommonTableData) {
      return '100px';
    }
    return calculateRelationshipWidth(this.uploadFileCommonTableData, relationshipName);
  }

  tableWidth(): string {
    return calculateTableWidth(this.uploadFileCommonTableData!);
  }
}
