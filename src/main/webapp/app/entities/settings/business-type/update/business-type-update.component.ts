import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
// import * as moment from 'moment';

import { IBusinessType } from '../business-type.model';
import { BusinessTypeService } from '../service/business-type.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-business-type-update',
  templateUrl: './business-type-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class BusinessTypeUpdateComponent implements OnInit {
  businessType: IBusinessType | null = null;
  isSaving = false;

  constructor(protected businessTypeService: BusinessTypeService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ businessType }) => {
      this.businessType = businessType;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (this.businessType?.id !== undefined) {
      this.subscribeToSaveResponse(this.businessTypeService.update(this.businessType));
    } else {
      this.subscribeToSaveResponse(this.businessTypeService.create(this.businessType!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusinessType>>): void {
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
}
