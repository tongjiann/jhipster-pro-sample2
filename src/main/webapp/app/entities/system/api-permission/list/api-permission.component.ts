import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
// import * as moment from 'moment';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';

import { IApiPermission, ApiPermission } from '../api-permission.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ApiPermissionService } from '../service/api-permission.service';
import { IDepartmentAuthority } from 'app/entities/settings/department-authority/department-authority.model';
import { DepartmentAuthorityService } from 'app/entities/settings/department-authority/service/department-authority.service';
import { IAuthority } from 'app/entities/system/authority/authority.model';
import { AuthorityService } from 'app/entities/system/authority/service/authority.service';
import { Observable, Subscription } from 'rxjs';
// import { filter, map } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import { ICommonTable } from 'app/entities/modelConfig/common-table/common-table.model';
import { CommonTableService } from 'app/entities/modelConfig/common-table/service/common-table.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';
import { calculateFieldWidth, calculateRelationshipWidth, calculateTableWidth } from 'app/shared/util/table-util';

@Component({
  selector: 'jhi-api-permission',
  templateUrl: './api-permission.component.html',
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
export class ApiPermissionComponent implements OnInit {
  sortName: string | null = null;
  sortValue: string | null = null;
  // 排序
  // value :"descend" or "ascend" or null
  mapOfSort: { [key: string]: any } = {};
  // 过滤
  searchValue = '';
  dateRange = [];
  listOfFilterStatus = [];
  listOfFilterStatusValue: string[] = [];
  apiPermissionCommonTableData: ICommonTable | null = null; // 实体模型数据
  mapOfFilter: { [key: string]: any } = {};
  // 待更新内容的字段列表
  specifiedFields = ['title', 'createAt', 'intro', 'pageViews', 'status', 'top', 'hot', 'columnId', 'picId', 'userId'];
  // 每行的可编辑状态
  editStatus: { [key: string]: any } = {};
  // 是否在表格内有省略的字段
  omitFields = ['id'];
  // 所有记录单选框状态
  mapOfCheckedId: { [key: string]: boolean } = {};
  // 是否所有记录都被选中
  isAllDisplayDataChecked = false;
  // 是否有选中有未选中的混合状态
  isIndeterminate = false;
  // 选中记录的总数
  numberOfChecked = 0;
  currentAccount: any;
  apiPermissions?: IApiPermission[];
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
  // treetable 1.
  expandDataCache: any = {};

  departmentAuthoritiesCollection: IDepartmentAuthority[] = [];

  apiPermissionsCollectionNzTreeNodes: any[] = [];

  apiPermissionsCollection: IApiPermission[] = [];

  authoritiesCollectionNzTreeNodes: any[] = [];

  authoritiesCollection: IAuthority[] = [];
  constructor(
    protected apiPermissionService: ApiPermissionService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    private translateService: TranslateService,
    protected router: Router,
    protected departmentAuthorityService: DepartmentAuthorityService,
    protected authorityService: AuthorityService,
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
            this.apiPermissionCommonTableData = data.commonTableData[0];
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

    this.apiPermissionService.tree(requestOption).subscribe(
      (res: HttpResponse<IApiPermission[]>) => {
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

    // 处理enum
    const listOfFiltertype: any[] = [];
    this.translateService.get('jhipsterSampleApplicationApp.ApiPermissionType.BUSINESS').subscribe(text => {
      listOfFiltertype.push({ text, value: 'BUSINESS' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.ApiPermissionType.API').subscribe(text => {
      listOfFiltertype.push({ text, value: 'API' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.ApiPermissionType.ENTITY').subscribe(text => {
      listOfFiltertype.push({ text, value: 'ENTITY' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.ApiPermissionType.MICRO_SERVICE').subscribe(text => {
      listOfFiltertype.push({ text, value: 'MICRO_SERVICE' });
    });
    this.mapOfFilter.type = { list: listOfFiltertype, value: [], type: 'Enum' };

    const listOfFilterstatus: any[] = [];
    this.translateService.get('jhipsterSampleApplicationApp.ApiPermissionState.CONFIGURABLE').subscribe(text => {
      listOfFilterstatus.push({ text, value: 'CONFIGURABLE' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.ApiPermissionState.PERMIT_ALL').subscribe(text => {
      listOfFilterstatus.push({ text, value: 'PERMIT_ALL' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.ApiPermissionState.UNREACHABLE').subscribe(text => {
      listOfFilterstatus.push({ text, value: 'UNREACHABLE' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.ApiPermissionState.AUTHENTICATE').subscribe(text => {
      listOfFilterstatus.push({ text, value: 'AUTHENTICATE' });
    });
    this.mapOfFilter.status = { list: listOfFilterstatus, value: [], type: 'Enum' };
  }

  trackDepartmentAuthorityById(index: number, item: IDepartmentAuthority) {
    return item.id;
  }

  trackApiPermissionById(index: number, item: IApiPermission) {
    return item.id;
  }

  trackAuthorityById(index: number, item: IAuthority) {
    return item.id;
  }
  trackId(index: number, item: IApiPermission) {
    return item.id;
  }

  deleteConfirm(id: number | undefined): void {
    if (!id) {
      return;
    }
    this.modalService.confirm({
      nzTitle: '是否要删除id为' + id + '的记录?',
      nzOkText: '删除',
      nzOkType: 'danger',
      nzOnOk: () => this.delete(id),
      nzCancelText: '取消',
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
      nzTitle: '是否要删除id为' + ids.join(';') + '的记录?',
      nzOkText: '删除',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteByIds(ids),
      nzCancelText: '取消',
    });
  }

  delete(id: number | undefined): void {
    if (!id) {
      return;
    }
    this.apiPermissionService.delete(id).subscribe(
      (res: HttpResponse<any>) => {
        this.msg.success('删除成功！');
        this.loadPage();
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  deleteByIds(ids: number[]): void {
    this.apiPermissionService.deleteByIds(ids).subscribe(
      (res: HttpResponse<any>) => {
        this.msg.success('批量删除成功！');
        this.loadPage();
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  protected paginateApiPermissions(data: IApiPermission[], headers: HttpHeaders) {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.queryCount = this.totalItems;
    // 设置每行为不可编辑状态，设置单选框未选状态
    data.forEach(apiPermission => {
      this.editStatus[apiPermission.id!] = {
        edit: false,
      };
      this.mapOfCheckedId[apiPermission.id!] = false;
    });
    this.apiPermissions = data;

    // treetable 5.
    this.apiPermissions.forEach(item => {
      this.expandDataCache[item.id!] = this.convertTreeToList(item);
    });
  }

  protected onError(errorMessage: string) {
    this.page = 0;
    this.msg.error(errorMessage);
  }

  // treetable 2.
  collapse(array: IApiPermission[], data: IApiPermission, $event: boolean): void {
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
  convertTreeToList(root: any): IApiPermission[] {
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
  visitNode(node: IApiPermission, hashMap: any, array: IApiPermission[]): void {
    if (!hashMap[node.id!]) {
      hashMap[node.id!] = true;
      array.push(node);
    }
  }
  saveEdit(apiPermission: IApiPermission) {
    this.isSaving = true;
    if (apiPermission.id !== undefined) {
      if (this.omitFields.length > 1) {
        this.subscribeToSaveResponse(this.apiPermissionService.updateBySpecifiedFields(apiPermission, this.omitFields));
      } else {
        this.subscribeToSaveResponse(this.apiPermissionService.update(apiPermission));
      }
    } else {
      this.subscribeToSaveResponse(this.apiPermissionService.create(apiPermission));
    }
    this.editStatus[apiPermission.id!].edit = false;
  }
  cancelEdit(id: number | undefined): void {
    if (id) {
      this.editStatus[id].edit = false;
      this.loadPage();
    }
  }
  // 新增 并设置为可编辑状态
  newApiPermission() {
    const apiPermission = new ApiPermission();
    apiPermission.id = -1;
    this.apiPermissions?.push(apiPermission);
    this.editStatus[apiPermission.id].edit = true;
  }
  // 设置某行可编辑
  startEdit(apiPermission: IApiPermission): void {
    this.editStatus[apiPermission.id!].edit = true;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApiPermission>>) {
    result.subscribe(
      (res: HttpResponse<IApiPermission>) => this.onSaveSuccess(),
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
  // 处理单选框状态
  refreshStatus(): void {
    this.isAllDisplayDataChecked = this.apiPermissions?.every(item => this.mapOfCheckedId[item.id!])!;
    this.isIndeterminate = this.apiPermissions?.some(item => this.mapOfCheckedId[item.id!])! && !this.isAllDisplayDataChecked;
    this.numberOfChecked = this.apiPermissions?.filter(item => this.mapOfCheckedId[item.id!]).length!;
  }

  checkAll(value: boolean): void {
    this.apiPermissions?.forEach(item => (this.mapOfCheckedId[item.id!] = value));
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

  protected onSuccess(data: IApiPermission[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/api-permission'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.apiPermissions = data ?? [];
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
    const fields = this.apiPermissionCommonTableData?.commonTableFields;
    if (!fields) {
      return true;
    }
    return fields?.some(field => field.entityFieldName === fieldName && !field.hideInList);
  }

  showRelationship(relationshipName: string): boolean {
    const relationships = this.apiPermissionCommonTableData?.relationships;
    if (!relationships) {
      return true;
    }
    return relationships?.some(relationship => relationship.relationshipName === relationshipName && !relationship.hideInList);
  }

  fieldWidth(fieldName: string): string {
    if (!this.apiPermissionCommonTableData) {
      return '100px';
    }
    return calculateFieldWidth(this.apiPermissionCommonTableData, fieldName);
  }

  relationshipWidth(relationshipName: string): string {
    if (!this.apiPermissionCommonTableData) {
      return '100px';
    }
    return calculateRelationshipWidth(this.apiPermissionCommonTableData, relationshipName);
  }

  tableWidth(): string {
    return calculateTableWidth(this.apiPermissionCommonTableData!);
  }
}
