import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
// import * as moment from 'moment';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';

import { IViewPermission, ViewPermission } from '../view-permission.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ViewPermissionService } from '../service/view-permission.service';
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
  selector: 'jhi-view-permission',
  templateUrl: './view-permission.component.html',
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
export class ViewPermissionComponent implements OnInit {
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
  viewPermissionCommonTableData: ICommonTable | null = null; // ??????????????????
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
  viewPermissions?: IViewPermission[];
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

  viewPermissionsCollectionNzTreeNodes: any[] = [];

  viewPermissionsCollection: IViewPermission[] = [];

  authoritiesCollectionNzTreeNodes: any[] = [];

  authoritiesCollection: IAuthority[] = [];
  constructor(
    protected viewPermissionService: ViewPermissionService,
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
            this.viewPermissionCommonTableData = data.commonTableData[0];
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

    this.viewPermissionService.tree(requestOption).subscribe(
      (res: HttpResponse<IViewPermission[]>) => {
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
    const listOfFiltertarget: any[] = [];
    this.translateService.get('jhipsterSampleApplicationApp.TargetType.BLANK').subscribe(text => {
      listOfFiltertarget.push({ text, value: 'BLANK' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.TargetType.SELF').subscribe(text => {
      listOfFiltertarget.push({ text, value: 'SELF' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.TargetType.PARENT').subscribe(text => {
      listOfFiltertarget.push({ text, value: 'PARENT' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.TargetType.TOP').subscribe(text => {
      listOfFiltertarget.push({ text, value: 'TOP' });
    });
    this.mapOfFilter.target = { list: listOfFiltertarget, value: [], type: 'Enum' };

    const listOfFiltertype: any[] = [];
    this.translateService.get('jhipsterSampleApplicationApp.ViewPermissionType.MENU').subscribe(text => {
      listOfFiltertype.push({ text, value: 'MENU' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.ViewPermissionType.BUTTON').subscribe(text => {
      listOfFiltertype.push({ text, value: 'BUTTON' });
    });
    this.mapOfFilter.type = { list: listOfFiltertype, value: [], type: 'Enum' };
  }

  trackDepartmentAuthorityById(index: number, item: IDepartmentAuthority) {
    return item.id;
  }

  trackViewPermissionById(index: number, item: IViewPermission) {
    return item.id;
  }

  trackAuthorityById(index: number, item: IAuthority) {
    return item.id;
  }
  trackId(index: number, item: IViewPermission) {
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
    this.viewPermissionService.delete(id).subscribe(
      (res: HttpResponse<any>) => {
        this.msg.success('???????????????');
        this.loadPage();
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  deleteByIds(ids: number[]): void {
    this.viewPermissionService.deleteByIds(ids).subscribe(
      (res: HttpResponse<any>) => {
        this.msg.success('?????????????????????');
        this.loadPage();
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  protected paginateViewPermissions(data: IViewPermission[], headers: HttpHeaders) {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.queryCount = this.totalItems;
    // ???????????????????????????????????????????????????????????????
    data.forEach(viewPermission => {
      this.editStatus[viewPermission.id!] = {
        edit: false,
      };
      this.mapOfCheckedId[viewPermission.id!] = false;
    });
    this.viewPermissions = data;

    // treetable 5.
    this.viewPermissions.forEach(item => {
      this.expandDataCache[item.id!] = this.convertTreeToList(item);
    });
  }

  protected onError(errorMessage: string) {
    this.page = 0;
    this.msg.error(errorMessage);
  }

  // treetable 2.
  collapse(array: IViewPermission[], data: IViewPermission, $event: boolean): void {
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
  convertTreeToList(root: any): IViewPermission[] {
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
  visitNode(node: IViewPermission, hashMap: any, array: IViewPermission[]): void {
    if (!hashMap[node.id!]) {
      hashMap[node.id!] = true;
      array.push(node);
    }
  }
  saveEdit(viewPermission: IViewPermission) {
    this.isSaving = true;
    if (viewPermission.id !== undefined) {
      if (this.omitFields.length > 1) {
        this.subscribeToSaveResponse(this.viewPermissionService.updateBySpecifiedFields(viewPermission, this.omitFields));
      } else {
        this.subscribeToSaveResponse(this.viewPermissionService.update(viewPermission));
      }
    } else {
      this.subscribeToSaveResponse(this.viewPermissionService.create(viewPermission));
    }
    this.editStatus[viewPermission.id!].edit = false;
  }
  cancelEdit(id: number | undefined): void {
    if (id) {
      this.editStatus[id].edit = false;
      this.loadPage();
    }
  }
  // ?????? ???????????????????????????
  newViewPermission() {
    const viewPermission = new ViewPermission();
    viewPermission.id = -1;
    this.viewPermissions?.push(viewPermission);
    this.editStatus[viewPermission.id].edit = true;
  }
  // ?????????????????????
  startEdit(viewPermission: IViewPermission): void {
    this.editStatus[viewPermission.id!].edit = true;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IViewPermission>>) {
    result.subscribe(
      (res: HttpResponse<IViewPermission>) => this.onSaveSuccess(),
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
    this.isAllDisplayDataChecked = this.viewPermissions?.every(item => this.mapOfCheckedId[item.id!])!;
    this.isIndeterminate = this.viewPermissions?.some(item => this.mapOfCheckedId[item.id!])! && !this.isAllDisplayDataChecked;
    this.numberOfChecked = this.viewPermissions?.filter(item => this.mapOfCheckedId[item.id!]).length!;
  }

  checkAll(value: boolean): void {
    this.viewPermissions?.forEach(item => (this.mapOfCheckedId[item.id!] = value));
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

  protected onSuccess(data: IViewPermission[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/view-permission'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.viewPermissions = data ?? [];
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
    const fields = this.viewPermissionCommonTableData?.commonTableFields;
    if (!fields) {
      return true;
    }
    return fields?.some(field => field.entityFieldName === fieldName && !field.hideInList);
  }

  showRelationship(relationshipName: string): boolean {
    const relationships = this.viewPermissionCommonTableData?.relationships;
    if (!relationships) {
      return true;
    }
    return relationships?.some(relationship => relationship.relationshipName === relationshipName && !relationship.hideInList);
  }

  fieldWidth(fieldName: string): string {
    if (!this.viewPermissionCommonTableData) {
      return '100px';
    }
    return calculateFieldWidth(this.viewPermissionCommonTableData, fieldName);
  }

  relationshipWidth(relationshipName: string): string {
    if (!this.viewPermissionCommonTableData) {
      return '100px';
    }
    return calculateRelationshipWidth(this.viewPermissionCommonTableData, relationshipName);
  }

  tableWidth(): string {
    return calculateTableWidth(this.viewPermissionCommonTableData!);
  }
}
