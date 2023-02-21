import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
// import * as moment from 'moment';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

import { IViewPermission } from '../view-permission.model';
import { ViewPermissionService } from '../service/view-permission.service';
import { IDepartmentAuthority } from 'app/entities/settings/department-authority/department-authority.model';
import { DepartmentAuthorityService } from 'app/entities/settings/department-authority/service/department-authority.service';
import { IAuthority } from 'app/entities/system/authority/authority.model';
import { AuthorityService } from 'app/entities/system/authority/service/authority.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-view-permission-update',
  templateUrl: './view-permission-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class ViewPermissionUpdateComponent implements OnInit {
  viewPermission: IViewPermission | null = null;
  isSaving = false;

  departmentAuthoritiesCollection: IDepartmentAuthority[] = [];

  viewPermissionsCollectionNzTreeNodes: any[] = [];

  viewPermissionsCollection: IViewPermission[] = [];

  authoritiesCollectionNzTreeNodes: any[] = [];

  authoritiesCollection: IAuthority[] = [];

  constructor(
    protected eventManager: EventManager,
    protected viewPermissionService: ViewPermissionService,
    protected departmentAuthorityService: DepartmentAuthorityService,
    protected authorityService: AuthorityService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ viewPermission }) => {
      this.viewPermission = viewPermission;
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
          this.viewPermissionsCollectionNzTreeNodes = toNzTreeNodeKeyId(
            this.viewPermissionsCollection,
            this.viewPermission?.parent?.id,
            'text',
            this.viewPermission?.id
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
          this.authoritiesCollectionNzTreeNodes = toNzTreeNode(this.authoritiesCollection, this.viewPermission?.authorities, 'name');
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (this.viewPermission?.id !== undefined) {
      this.subscribeToSaveResponse(this.viewPermissionService.update(this.viewPermission));
    } else {
      this.subscribeToSaveResponse(this.viewPermissionService.create(this.viewPermission!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IViewPermission>>): void {
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

  trackViewPermissionById(index: number, item: IViewPermission): number {
    return item.id!;
  }

  trackDepartmentAuthorityById(index: number, item: IDepartmentAuthority): number {
    return item.id!;
  }
}
