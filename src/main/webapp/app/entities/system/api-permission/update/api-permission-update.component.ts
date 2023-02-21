import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
// import * as moment from 'moment';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

import { IApiPermission } from '../api-permission.model';
import { ApiPermissionService } from '../service/api-permission.service';
import { IDepartmentAuthority } from 'app/entities/settings/department-authority/department-authority.model';
import { DepartmentAuthorityService } from 'app/entities/settings/department-authority/service/department-authority.service';
import { IAuthority } from 'app/entities/system/authority/authority.model';
import { AuthorityService } from 'app/entities/system/authority/service/authority.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-api-permission-update',
  templateUrl: './api-permission-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class ApiPermissionUpdateComponent implements OnInit {
  apiPermission: IApiPermission | null = null;
  isSaving = false;

  departmentAuthoritiesCollection: IDepartmentAuthority[] = [];

  apiPermissionsCollectionNzTreeNodes: any[] = [];

  apiPermissionsCollection: IApiPermission[] = [];

  authoritiesCollectionNzTreeNodes: any[] = [];

  authoritiesCollection: IAuthority[] = [];

  constructor(
    protected eventManager: EventManager,
    protected apiPermissionService: ApiPermissionService,
    protected departmentAuthorityService: DepartmentAuthorityService,
    protected authorityService: AuthorityService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ apiPermission }) => {
      this.apiPermission = apiPermission;
    });
    this.departmentAuthorityService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDepartmentAuthority[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDepartmentAuthority[]>) => response.body)
      )
      .subscribe(
        (res: IDepartmentAuthority[] | null) => {
          this.departmentAuthoritiesCollection = res!;
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
          this.apiPermissionsCollectionNzTreeNodes = toNzTreeNodeKeyId(
            this.apiPermissionsCollection,
            this.apiPermission?.parent?.id,
            'name',
            this.apiPermission?.id
          );
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
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
          this.authoritiesCollectionNzTreeNodes = toNzTreeNode(this.authoritiesCollection, this.apiPermission?.authorities, 'name');
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (this.apiPermission?.id !== undefined) {
      this.subscribeToSaveResponse(this.apiPermissionService.update(this.apiPermission));
    } else {
      this.subscribeToSaveResponse(this.apiPermissionService.create(this.apiPermission!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApiPermission>>): void {
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

  trackApiPermissionById(index: number, item: IApiPermission): number {
    return item.id!;
  }

  trackDepartmentAuthorityById(index: number, item: IDepartmentAuthority): number {
    return item.id!;
  }
}
