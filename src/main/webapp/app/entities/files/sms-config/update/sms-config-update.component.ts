import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
// import * as moment from 'moment';

import { ISmsConfig } from '../sms-config.model';
import { SmsConfigService } from '../service/sms-config.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-sms-config-update',
  templateUrl: './sms-config-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class SmsConfigUpdateComponent implements OnInit {
  smsConfig: ISmsConfig | null = null;
  isSaving = false;

  constructor(protected smsConfigService: SmsConfigService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ smsConfig }) => {
      this.smsConfig = smsConfig;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (this.smsConfig?.id !== undefined) {
      this.subscribeToSaveResponse(this.smsConfigService.update(this.smsConfig));
    } else {
      this.subscribeToSaveResponse(this.smsConfigService.create(this.smsConfig!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmsConfig>>): void {
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
