import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
// import * as moment from 'moment';

import { ISmsTemplate } from '../sms-template.model';
import { SmsTemplateService } from '../service/sms-template.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-sms-template-update',
  templateUrl: './sms-template-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class SmsTemplateUpdateComponent implements OnInit {
  smsTemplate: ISmsTemplate | null = null;
  isSaving = false;

  constructor(protected smsTemplateService: SmsTemplateService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ smsTemplate }) => {
      this.smsTemplate = smsTemplate;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (this.smsTemplate?.id !== undefined) {
      this.subscribeToSaveResponse(this.smsTemplateService.update(this.smsTemplate));
    } else {
      this.subscribeToSaveResponse(this.smsTemplateService.create(this.smsTemplate!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmsTemplate>>): void {
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
