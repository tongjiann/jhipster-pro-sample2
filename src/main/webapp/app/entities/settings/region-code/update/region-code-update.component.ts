import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
// import * as moment from 'moment';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

import { IRegionCode } from '../region-code.model';
import { RegionCodeService } from '../service/region-code.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-region-code-update',
  templateUrl: './region-code-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class RegionCodeUpdateComponent implements OnInit {
  regionCode: IRegionCode | null = null;
  isSaving = false;

  regionCodesCollectionNzTreeNodes: any[] = [];

  regionCodesCollection: IRegionCode[] = [];

  constructor(
    protected eventManager: EventManager,
    protected regionCodeService: RegionCodeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ regionCode }) => {
      this.regionCode = regionCode;
    });
    this.regionCodeService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IRegionCode[]>) => mayBeOk.ok),
        map((response: HttpResponse<IRegionCode[]>) => response.body)
      )
      .subscribe(
        (res: IRegionCode[] | null) => {
          this.regionCodesCollection = res!;
          // add by wang xin
          this.regionCodesCollectionNzTreeNodes = toNzTreeNodeKeyId(
            this.regionCodesCollection,
            this.regionCode?.parent?.id,
            'name',
            this.regionCode?.id
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
    if (this.regionCode?.id !== undefined) {
      this.subscribeToSaveResponse(this.regionCodeService.update(this.regionCode));
    } else {
      this.subscribeToSaveResponse(this.regionCodeService.create(this.regionCode!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegionCode>>): void {
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

  trackRegionCodeById(index: number, item: IRegionCode): number {
    return item.id!;
  }
}
