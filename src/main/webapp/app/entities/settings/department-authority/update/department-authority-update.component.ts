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

import { IDepartmentAuthority } from '../department-authority.model';
import { DepartmentAuthorityService } from '../service/department-authority.service';
import { IDepartment } from 'app/entities/settings/department/department.model';
import { DepartmentService } from 'app/entities/settings/department/service/department.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-department-authority-update',
  templateUrl: './department-authority-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class DepartmentAuthorityUpdateComponent implements OnInit {
  departmentAuthority: IDepartmentAuthority | null = null;
  isSaving = false;

  departmentsCollectionNzTreeNodes: any[] = [];

  departmentsCollection: IDepartment[] = [];

  constructor(
    protected eventManager: EventManager,
    protected departmentAuthorityService: DepartmentAuthorityService,
    protected departmentService: DepartmentService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ departmentAuthority }) => {
      this.departmentAuthority = departmentAuthority;

      // this.createTime = this.departmentAuthority.createTime != null ? this.departmentAuthority.createTime.format(DATE_TIME_FORMAT) : null;
    });
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
            this.departmentAuthority?.department?.id,
            'name'
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
    // this.departmentAuthority.createTime = this.createTime != null ? moment(this.createTime, DATE_TIME_FORMAT) : null;
    if (this.departmentAuthority?.id !== undefined) {
      this.subscribeToSaveResponse(this.departmentAuthorityService.update(this.departmentAuthority));
    } else {
      this.subscribeToSaveResponse(this.departmentAuthorityService.create(this.departmentAuthority!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartmentAuthority>>): void {
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
}
