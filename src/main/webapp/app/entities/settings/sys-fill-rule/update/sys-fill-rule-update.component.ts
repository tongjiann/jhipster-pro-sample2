import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
// import * as moment from 'moment';

import { ISysFillRule } from '../sys-fill-rule.model';
import { SysFillRuleService } from '../service/sys-fill-rule.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-sys-fill-rule-update',
  templateUrl: './sys-fill-rule-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class SysFillRuleUpdateComponent implements OnInit {
  sysFillRule: ISysFillRule | null = null;
  isSaving = false;

  constructor(protected sysFillRuleService: SysFillRuleService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ sysFillRule }) => {
      this.sysFillRule = sysFillRule;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (this.sysFillRule?.id !== undefined) {
      this.subscribeToSaveResponse(this.sysFillRuleService.update(this.sysFillRule));
    } else {
      this.subscribeToSaveResponse(this.sysFillRuleService.create(this.sysFillRule!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISysFillRule>>): void {
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
