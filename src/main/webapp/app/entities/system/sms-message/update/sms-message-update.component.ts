import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
// import * as moment from 'moment';
// import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

import { ISmsMessage } from '../sms-message.model';
import { SmsMessageService } from '../service/sms-message.service';
import { UPLOAD_IMAGE_URL } from 'app/app.constants';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { toNzTreeNode, toNzTreeNodeKeyId } from 'app/shared/util/tree-util';

@Component({
  selector: 'jhi-sms-message-update',
  templateUrl: './sms-message-update.component.html',
  styles: [
    `
      .ant-form-item {
        width: 44%;
      }
    `,
  ],
})
export class SmsMessageUpdateComponent implements OnInit {
  smsMessage: ISmsMessage | null = null;
  isSaving = false;
  froalaOptions = {
    language: 'zh_cn',
    placeholderText: '请输入内容',
    charCounterCount: true,
    codeMirror: true,
    codeMirrorOptions: {
      tabSize: 4,
    },
    imageUploadParam: 'image',
    requestHeaders: {
      Authorization: 'Bearer ' + this.authServerProvider.getToken(),
    },
    imageUploadURL: UPLOAD_IMAGE_URL,
    imageUploadParams: {},
    imageUploadMethod: 'POST',
    toolbarButtons: {
      moreText: {
        buttons: [
          'bold',
          'italic',
          'underline',
          'strikeThrough',
          'subscript',
          'superscript',
          'fontFamily',
          'fontSize',
          'textColor',
          'backgroundColor',
          'inlineClass',
          'inlineStyle',
          'clearFormatting',
        ],
      },
      moreParagraph: {
        buttons: [
          'alignLeft',
          'alignCenter',
          'formatOLSimple',
          'alignRight',
          'alignJustify',
          'formatOL',
          'formatUL',
          'paragraphFormat',
          'paragraphStyle',
          'lineHeight',
          'outdent',
          'indent',
          'quote',
        ],
      },
      moreRich: {
        buttons: [
          'insertLink',
          'insertImage',
          'insertVideo',
          'insertTable',
          'emoticons',
          'fontAwesome',
          'specialCharacters',
          'embedly',
          'insertFile',
          'insertHR',
        ],
      },
      moreMisc: {
        buttons: ['undo', 'redo', 'fullscreen', 'print', 'getPDF', 'spellChecker', 'selectAll', 'html', 'help'],
        align: 'right',
        buttonsVisible: 2,
      },
    },
  };

  constructor(
    protected dataUtils: DataUtils,
    protected smsMessageService: SmsMessageService,
    private authServerProvider: AuthServerProvider,
    protected activatedRoute: ActivatedRoute
  ) {
    this.froalaOptions['requestHeaders'] = {
      Authorization: 'Bearer ' + this.authServerProvider.getToken(),
    };
  }

  ngOnInit(): void {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ smsMessage }) => {
      this.smsMessage = smsMessage;
      // this.sendTime = this.smsMessage.sendTime != null ? this.smsMessage.sendTime.format(DATE_TIME_FORMAT) : null;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    /* this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
            error: (err: FileLoadError) =>
                this.eventManager.broadcast(
                    new EventWithContent<AlertError>('jhipsterSampleApplicationApp.error', { ...err, key: 'error.file.' + err.key })
                )
        }); */
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    // this.smsMessage.sendTime = this.sendTime != null ? moment(this.sendTime, DATE_TIME_FORMAT) : null;
    if (this.smsMessage?.id !== undefined) {
      this.subscribeToSaveResponse(this.smsMessageService.update(this.smsMessage));
    } else {
      this.subscribeToSaveResponse(this.smsMessageService.create(this.smsMessage!));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmsMessage>>): void {
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
