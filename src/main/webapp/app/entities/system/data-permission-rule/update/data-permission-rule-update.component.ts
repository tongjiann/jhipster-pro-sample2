import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
// import * as moment from 'moment';

import { IDataPermissionRule } from '../data-permission-rule.model';
import { DataPermissionRuleService } from '../service/data-permission-rule.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-data-permission-rule-update',
  templateUrl: './data-permission-rule-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class DataPermissionRuleUpdateComponent implements OnInit {
  dataPermissionRule: IDataPermissionRule | null = null;
  isSaving = false;

  constructor(protected dataPermissionRuleService: DataPermissionRuleService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ dataPermissionRule }) => {
      this.dataPermissionRule = dataPermissionRule;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (this.dataPermissionRule?.id !== undefined) {
      this.subscribeToSaveResponse(this.dataPermissionRuleService.update(this.dataPermissionRule));
    } else {
      this.subscribeToSaveResponse(this.dataPermissionRuleService.create(this.dataPermissionRule!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDataPermissionRule>>): void {
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
