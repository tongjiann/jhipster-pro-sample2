import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
// import * as moment from 'moment';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

import { IDataDictionary } from '../data-dictionary.model';
import { DataDictionaryService } from '../service/data-dictionary.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-data-dictionary-update',
  templateUrl: './data-dictionary-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class DataDictionaryUpdateComponent implements OnInit {
  dataDictionary: IDataDictionary | null = null;
  isSaving = false;

  dataDictionariesCollectionNzTreeNodes: any[] = [];

  dataDictionariesCollection: IDataDictionary[] = [];

  constructor(
    protected eventManager: EventManager,
    protected dataDictionaryService: DataDictionaryService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ dataDictionary }) => {
      this.dataDictionary = dataDictionary;
    });
    this.dataDictionaryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDataDictionary[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDataDictionary[]>) => response.body)
      )
      .subscribe(
        (res: IDataDictionary[] | null) => {
          this.dataDictionariesCollection = res!;
          // add by wang xin
          this.dataDictionariesCollectionNzTreeNodes = toNzTreeNodeKeyId(
            this.dataDictionariesCollection,
            this.dataDictionary?.parent?.id,
            'name',
            this.dataDictionary?.id
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
    if (this.dataDictionary?.id !== undefined) {
      this.subscribeToSaveResponse(this.dataDictionaryService.update(this.dataDictionary));
    } else {
      this.subscribeToSaveResponse(this.dataDictionaryService.create(this.dataDictionary!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDataDictionary>>): void {
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

  trackDataDictionaryById(index: number, item: IDataDictionary): number {
    return item.id!;
  }
}
