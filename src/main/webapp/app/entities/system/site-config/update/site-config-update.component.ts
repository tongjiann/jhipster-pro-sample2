import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
// import * as moment from 'moment';

import { ISiteConfig } from '../site-config.model';
import { SiteConfigService } from '../service/site-config.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-site-config-update',
  templateUrl: './site-config-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class SiteConfigUpdateComponent implements OnInit {
  siteConfig: ISiteConfig | null = null;
  isSaving = false;

  constructor(protected siteConfigService: SiteConfigService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ siteConfig }) => {
      this.siteConfig = siteConfig;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (this.siteConfig?.id !== undefined) {
      this.subscribeToSaveResponse(this.siteConfigService.update(this.siteConfig));
    } else {
      this.subscribeToSaveResponse(this.siteConfigService.create(this.siteConfig!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISiteConfig>>): void {
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
