import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
// import * as moment from 'moment';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';

import { IDataPermissionRule, DataPermissionRule } from '../data-permission-rule.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DataPermissionRuleService } from '../service/data-permission-rule.service';
import { Observable, Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { ICommonTable } from 'app/entities/modelConfig/common-table/common-table.model';
import { CommonTableService } from 'app/entities/modelConfig/common-table/service/common-table.service';
import { calculateFieldWidth, calculateRelationshipWidth, calculateTableWidth } from 'app/shared/util/table-util';

@Component({
  selector: 'jhi-data-permission-rule',
  templateUrl: './data-permission-rule.component.html',
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
export class DataPermissionRuleComponent implements OnInit {
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
  dataPermissionRuleCommonTableData: ICommonTable | null = null; // 实体模型数据
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
  dataPermissionRules?: IDataPermissionRule[];
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
  constructor(
    protected dataPermissionRuleService: DataPermissionRuleService,
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
            this.dataPermissionRuleCommonTableData = data.commonTableData[0];
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

    this.dataPermissionRuleService.query(requestOption).subscribe(
      (res: HttpResponse<IDataPermissionRule[]>) => {
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
  }
  trackId(index: number, item: IDataPermissionRule) {
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
    this.dataPermissionRuleService.delete(id).subscribe(
      (res: HttpResponse<any>) => {
        this.msg.success('删除成功！');
        this.loadPage();
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  deleteByIds(ids: number[]): void {
    this.dataPermissionRuleService.deleteByIds(ids).subscribe(
      (res: HttpResponse<any>) => {
        this.msg.success('批量删除成功！');
        this.loadPage();
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  protected paginateDataPermissionRules(data: IDataPermissionRule[], headers: HttpHeaders) {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.queryCount = this.totalItems;
    // 设置每行为不可编辑状态，设置单选框未选状态
    data.forEach(dataPermissionRule => {
      this.editStatus[dataPermissionRule.id!] = {
        edit: false,
      };
      this.mapOfCheckedId[dataPermissionRule.id!] = false;
    });
    this.dataPermissionRules = data;
  }

  protected onError(errorMessage: string) {
    this.page = 0;
    this.msg.error(errorMessage);
  }

  saveEdit(dataPermissionRule: IDataPermissionRule) {
    this.isSaving = true;
    if (dataPermissionRule.id !== undefined) {
      if (this.omitFields.length > 1) {
        this.subscribeToSaveResponse(this.dataPermissionRuleService.updateBySpecifiedFields(dataPermissionRule, this.omitFields));
      } else {
        this.subscribeToSaveResponse(this.dataPermissionRuleService.update(dataPermissionRule));
      }
    } else {
      this.subscribeToSaveResponse(this.dataPermissionRuleService.create(dataPermissionRule));
    }
    this.editStatus[dataPermissionRule.id!].edit = false;
  }
  cancelEdit(id: number | undefined): void {
    if (id) {
      this.editStatus[id].edit = false;
      this.loadPage();
    }
  }
  // 新增 并设置为可编辑状态
  newDataPermissionRule() {
    const dataPermissionRule = new DataPermissionRule();
    dataPermissionRule.id = -1;
    this.dataPermissionRules?.push(dataPermissionRule);
    this.editStatus[dataPermissionRule.id].edit = true;
  }
  // 设置某行可编辑
  startEdit(dataPermissionRule: IDataPermissionRule): void {
    this.editStatus[dataPermissionRule.id!].edit = true;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDataPermissionRule>>) {
    result.subscribe(
      (res: HttpResponse<IDataPermissionRule>) => this.onSaveSuccess(),
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
    this.isAllDisplayDataChecked = this.dataPermissionRules?.every(item => this.mapOfCheckedId[item.id!])!;
    this.isIndeterminate = this.dataPermissionRules?.some(item => this.mapOfCheckedId[item.id!])! && !this.isAllDisplayDataChecked;
    this.numberOfChecked = this.dataPermissionRules?.filter(item => this.mapOfCheckedId[item.id!]).length!;
  }

  checkAll(value: boolean): void {
    this.dataPermissionRules?.forEach(item => (this.mapOfCheckedId[item.id!] = value));
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

  protected onSuccess(data: IDataPermissionRule[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/data-permission-rule'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.dataPermissionRules = data ?? [];
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
    const fields = this.dataPermissionRuleCommonTableData?.commonTableFields;
    if (!fields) {
      return true;
    }
    return fields?.some(field => field.entityFieldName === fieldName && !field.hideInList);
  }

  showRelationship(relationshipName: string): boolean {
    const relationships = this.dataPermissionRuleCommonTableData?.relationships;
    if (!relationships) {
      return true;
    }
    return relationships?.some(relationship => relationship.relationshipName === relationshipName && !relationship.hideInList);
  }

  fieldWidth(fieldName: string): string {
    if (!this.dataPermissionRuleCommonTableData) {
      return '100px';
    }
    return calculateFieldWidth(this.dataPermissionRuleCommonTableData, fieldName);
  }

  relationshipWidth(relationshipName: string): string {
    if (!this.dataPermissionRuleCommonTableData) {
      return '100px';
    }
    return calculateRelationshipWidth(this.dataPermissionRuleCommonTableData, relationshipName);
  }

  tableWidth(): string {
    return calculateTableWidth(this.dataPermissionRuleCommonTableData!);
  }
}
