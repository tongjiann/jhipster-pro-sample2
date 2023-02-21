import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
// import * as moment from 'moment';
// import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAnnouncementRecord } from '../announcement-record.model';
import { AnnouncementRecordService } from '../service/announcement-record.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-announcement-record-update',
  templateUrl: './announcement-record-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class AnnouncementRecordUpdateComponent implements OnInit {
  announcementRecord: IAnnouncementRecord | null = null;
  isSaving = false;

  constructor(protected announcementRecordService: AnnouncementRecordService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ announcementRecord }) => {
      this.announcementRecord = announcementRecord;
      // this.readTime = this.announcementRecord.readTime != null ? this.announcementRecord.readTime.format(DATE_TIME_FORMAT) : null;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    // this.announcementRecord.readTime = this.readTime != null ? moment(this.readTime, DATE_TIME_FORMAT) : null;
    if (this.announcementRecord?.id !== undefined) {
      this.subscribeToSaveResponse(this.announcementRecordService.update(this.announcementRecord));
    } else {
      this.subscribeToSaveResponse(this.announcementRecordService.create(this.announcementRecord!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnouncementRecord>>): void {
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
