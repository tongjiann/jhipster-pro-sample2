import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
// import * as moment from 'moment';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

import { IAuthority } from '../authority.model';
import { AuthorityService } from '../service/authority.service';
import { IDepartment } from 'app/entities/settings/department/department.model';
import { DepartmentService } from 'app/entities/settings/department/service/department.service';
import { IApiPermission } from 'app/entities/system/api-permission/api-permission.model';
import { ApiPermissionService } from 'app/entities/system/api-permission/service/api-permission.service';
import { IViewPermission } from 'app/entities/system/view-permission/view-permission.model';
import { ViewPermissionService } from 'app/entities/system/view-permission/service/view-permission.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-authority-update',
  templateUrl: './authority-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class AuthorityUpdateComponent implements OnInit {
  authority: IAuthority | null = null;
  isSaving = false;

  authoritiesCollectionNzTreeNodes: any[] = [];

  authoritiesCollection: IAuthority[] = [];

  departmentsCollectionNzTreeNodes: any[] = [];

  departmentsCollection: IDepartment[] = [];

  apiPermissionsCollectionNzTreeNodes: any[] = [];

  apiPermissionsCollection: IApiPermission[] = [];

  viewPermissionsCollectionNzTreeNodes: any[] = [];

  viewPermissionsCollection: IViewPermission[] = [];

  constructor(
    protected eventManager: EventManager,
    protected authorityService: AuthorityService,
    protected departmentService: DepartmentService,
    protected apiPermissionService: ApiPermissionService,
    protected viewPermissionService: ViewPermissionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ authority }) => {
      this.authority = authority;
    });
    this.authorityService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IAuthority[]>) => mayBeOk.ok),
        map((response: HttpResponse<IAuthority[]>) => response.body)
      )
      .subscribe(
        (res: IAuthority[] | null) => {
          this.authoritiesCollection = res!;
          // add by wang xin
          this.authoritiesCollectionNzTreeNodes = toNzTreeNodeKeyId(
            this.authoritiesCollection,
            this.authority?.parent?.id,
            'name',
            this.authority?.id
          );
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.departmentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDepartment[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDepartment[]>) => response.body)
      )
      .subscribe(
        (res: IDepartment[] | null) => {
          this.departmentsCollection = res!;
          // add by wang xin
          this.departmentsCollectionNzTreeNodes = toNzTreeNode(this.departmentsCollection, this.authority?.departments, 'name');
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.apiPermissionService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IApiPermission[]>) => mayBeOk.ok),
        map((response: HttpResponse<IApiPermission[]>) => response.body)
      )
      .subscribe(
        (res: IApiPermission[] | null) => {
          this.apiPermissionsCollection = res!;
          // add by wang xin
          this.apiPermissionsCollectionNzTreeNodes = toNzTreeNode(this.apiPermissionsCollection, this.authority?.apiPermissions, 'name');
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.viewPermissionService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IViewPermission[]>) => mayBeOk.ok),
        map((response: HttpResponse<IViewPermission[]>) => response.body)
      )
      .subscribe(
        (res: IViewPermission[] | null) => {
          this.viewPermissionsCollection = res!;
          // add by wang xin
          this.viewPermissionsCollectionNzTreeNodes = toNzTreeNode(this.viewPermissionsCollection, this.authority?.viewPermissions, 'text');
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (this.authority?.id !== undefined) {
      this.subscribeToSaveResponse(this.authorityService.update(this.authority));
    } else {
      this.subscribeToSaveResponse(this.authorityService.create(this.authority!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAuthority>>): void {
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

  trackAuthorityById(index: number, item: IAuthority): number {
    return item.id!;
  }

  trackApiPermissionById(index: number, item: IApiPermission): number {
    return item.id!;
  }

  trackViewPermissionById(index: number, item: IViewPermission): number {
    return item.id!;
  }

  getSelectedApiPermission(option: IApiPermission, selectedVals?: IApiPermission[] | null): IApiPermission {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedViewPermission(option: IViewPermission, selectedVals?: IViewPermission[] | null): IViewPermission {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }
}
