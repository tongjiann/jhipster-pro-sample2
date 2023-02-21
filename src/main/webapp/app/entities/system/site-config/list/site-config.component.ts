import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
// import * as moment from 'moment';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';

import { ISiteConfig, SiteConfig } from '../site-config.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { SiteConfigService } from '../service/site-config.service';
import { Observable, Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { ICommonTable } from 'app/entities/modelConfig/common-table/common-table.model';
import { CommonTableService } from 'app/entities/modelConfig/common-table/service/common-table.service';
import { calculateFieldWidth, calculateRelationshipWidth, calculateTableWidth } from 'app/shared/util/table-util';

@Component({
  selector: 'jhi-site-config',
  templateUrl: './site-config.component.html',
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
export class SiteConfigComponent implements OnInit {
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
  siteConfigCommonTableData: ICommonTable | null = null; // 实体模型数据
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
  siteConfigs?: ISiteConfig[];
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
    protected siteConfigService: SiteConfigService,
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
            this.siteConfigCommonTableData = data.commonTableData[0];
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

    this.siteConfigService.query(requestOption).subscribe(
      (res: HttpResponse<ISiteConfig[]>) => {
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
    const listOfFilterfieldType: any[] = [];
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.INTEGER').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'INTEGER' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.LONG').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'LONG' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.BOOLEAN').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'BOOLEAN' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.STRING').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'STRING' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.FLOAT').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'FLOAT' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.DOUBLE').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'DOUBLE' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.ZONED_DATE_TIME').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'ZONED_DATE_TIME' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.LOCATE_DATE').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'LOCATE_DATE' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.BIG_DECIMAL').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'BIG_DECIMAL' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.TEXTBLOB').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'TEXTBLOB' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.IMAGEBLOB').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'IMAGEBLOB' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.ARRAY').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'ARRAY' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.ENUM').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'ENUM' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.UPLOAD_IMAGE').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'UPLOAD_IMAGE' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.UPLOAD_FILE').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'UPLOAD_FILE' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.ENTITY').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'ENTITY' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.RADIO').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'RADIO' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.MULTI_SELECT').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'MULTI_SELECT' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.DATA_DICTIONARY').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'DATA_DICTIONARY' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.UUID').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'UUID' });
    });
    this.translateService.get('jhipsterSampleApplicationApp.CommonFieldType.INSTANT').subscribe(text => {
      listOfFilterfieldType.push({ text, value: 'INSTANT' });
    });
    this.mapOfFilter.fieldType = { list: listOfFilterfieldType, value: [], type: 'Enum' };
  }
  trackId(index: number, item: ISiteConfig) {
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
    this.siteConfigService.delete(id).subscribe(
      (res: HttpResponse<any>) => {
        this.msg.success('删除成功！');
        this.loadPage();
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  deleteByIds(ids: number[]): void {
    this.siteConfigService.deleteByIds(ids).subscribe(
      (res: HttpResponse<any>) => {
        this.msg.success('批量删除成功！');
        this.loadPage();
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  protected paginateSiteConfigs(data: ISiteConfig[], headers: HttpHeaders) {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.queryCount = this.totalItems;
    // 设置每行为不可编辑状态，设置单选框未选状态
    data.forEach(siteConfig => {
      this.editStatus[siteConfig.id!] = {
        edit: false,
      };
      this.mapOfCheckedId[siteConfig.id!] = false;
    });
    this.siteConfigs = data;
  }

  protected onError(errorMessage: string) {
    this.page = 0;
    this.msg.error(errorMessage);
  }

  saveEdit(siteConfig: ISiteConfig) {
    this.isSaving = true;
    if (siteConfig.id !== undefined) {
      if (this.omitFields.length > 1) {
        this.subscribeToSaveResponse(this.siteConfigService.updateBySpecifiedFields(siteConfig, this.omitFields));
      } else {
        this.subscribeToSaveResponse(this.siteConfigService.update(siteConfig));
      }
    } else {
      this.subscribeToSaveResponse(this.siteConfigService.create(siteConfig));
    }
    this.editStatus[siteConfig.id!].edit = false;
  }
  cancelEdit(id: number | undefined): void {
    if (id) {
      this.editStatus[id].edit = false;
      this.loadPage();
    }
  }
  // 新增 并设置为可编辑状态
  newSiteConfig() {
    const siteConfig = new SiteConfig();
    siteConfig.id = -1;
    this.siteConfigs?.push(siteConfig);
    this.editStatus[siteConfig.id].edit = true;
  }
  // 设置某行可编辑
  startEdit(siteConfig: ISiteConfig): void {
    this.editStatus[siteConfig.id!].edit = true;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISiteConfig>>) {
    result.subscribe(
      (res: HttpResponse<ISiteConfig>) => this.onSaveSuccess(),
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
    this.isAllDisplayDataChecked = this.siteConfigs?.every(item => this.mapOfCheckedId[item.id!])!;
    this.isIndeterminate = this.siteConfigs?.some(item => this.mapOfCheckedId[item.id!])! && !this.isAllDisplayDataChecked;
    this.numberOfChecked = this.siteConfigs?.filter(item => this.mapOfCheckedId[item.id!]).length!;
  }

  checkAll(value: boolean): void {
    this.siteConfigs?.forEach(item => (this.mapOfCheckedId[item.id!] = value));
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

  protected onSuccess(data: ISiteConfig[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/site-config'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.siteConfigs = data ?? [];
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
    const fields = this.siteConfigCommonTableData?.commonTableFields;
    if (!fields) {
      return true;
    }
    return fields?.some(field => field.entityFieldName === fieldName && !field.hideInList);
  }

  showRelationship(relationshipName: string): boolean {
    const relationships = this.siteConfigCommonTableData?.relationships;
    if (!relationships) {
      return true;
    }
    return relationships?.some(relationship => relationship.relationshipName === relationshipName && !relationship.hideInList);
  }

  fieldWidth(fieldName: string): string {
    if (!this.siteConfigCommonTableData) {
      return '100px';
    }
    return calculateFieldWidth(this.siteConfigCommonTableData, fieldName);
  }

  relationshipWidth(relationshipName: string): string {
    if (!this.siteConfigCommonTableData) {
      return '100px';
    }
    return calculateRelationshipWidth(this.siteConfigCommonTableData, relationshipName);
  }

  tableWidth(): string {
    return calculateTableWidth(this.siteConfigCommonTableData!);
  }
}
