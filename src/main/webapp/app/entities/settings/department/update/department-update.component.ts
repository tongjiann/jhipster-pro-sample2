import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
// import * as moment from 'moment';
// import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

import { IDepartment } from '../department.model';
import { DepartmentService } from '../service/department.service';
import { IAuthority } from 'app/entities/system/authority/authority.model';
import { AuthorityService } from 'app/entities/system/authority/service/authority.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-department-update',
  templateUrl: './department-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class DepartmentUpdateComponent implements OnInit {
  department: IDepartment | null = null;
  isSaving = false;

  authoritiesCollectionNzTreeNodes: any[] = [];

  authoritiesCollection: IAuthority[] = [];

  departmentsCollectionNzTreeNodes: any[] = [];

  departmentsCollection: IDepartment[] = [];

  constructor(
    protected eventManager: EventManager,
    protected departmentService: DepartmentService,
    protected authorityService: AuthorityService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ department }) => {
      this.department = department;

      // this.createTime = this.department.createTime != null ? this.department.createTime.format(DATE_TIME_FORMAT) : null;
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
          this.authoritiesCollectionNzTreeNodes = toNzTreeNode(this.authoritiesCollection, this.department?.authorities, 'name');
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
          this.departmentsCollectionNzTreeNodes = toNzTreeNodeKeyId(
            this.departmentsCollection,
            this.department?.parent?.id,
            'name',
            this.department?.id
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
    // this.department.createTime = this.createTime != null ? moment(this.createTime, DATE_TIME_FORMAT) : null;
    if (this.department?.id !== undefined) {
      this.subscribeToSaveResponse(this.departmentService.update(this.department));
    } else {
      this.subscribeToSaveResponse(this.departmentService.create(this.department!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartment>>): void {
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

  trackDepartmentById(index: number, item: IDepartment): number {
    return item.id!;
  }

  trackAuthorityById(index: number, item: IAuthority): number {
    return item.id!;
  }

  getSelectedAuthority(option: IAuthority, selectedVals?: IAuthority[] | null): IAuthority {
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
