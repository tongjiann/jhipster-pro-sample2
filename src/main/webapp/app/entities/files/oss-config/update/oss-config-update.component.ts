import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
// import * as moment from 'moment';

import { IOssConfig } from '../oss-config.model';
import { OssConfigService } from '../service/oss-config.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-oss-config-update',
  templateUrl: './oss-config-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class OssConfigUpdateComponent implements OnInit {
  ossConfig: IOssConfig | null = null;
  isSaving = false;

  constructor(protected ossConfigService: OssConfigService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ ossConfig }) => {
      this.ossConfig = ossConfig;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (this.ossConfig?.id !== undefined) {
      this.subscribeToSaveResponse(this.ossConfigService.update(this.ossConfig));
    } else {
      this.subscribeToSaveResponse(this.ossConfigService.create(this.ossConfig!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOssConfig>>): void {
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
