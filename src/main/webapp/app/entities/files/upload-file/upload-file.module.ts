import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UploadFileUpdateComponent } from './update/upload-file-update.component';
import { UploadFileSharedModule } from './upload-file-shared.module';
import { UploadFileRoutingModule } from './route/upload-file-routing.module';

@NgModule({
  imports: [SharedModule, UploadFileSharedModule, UploadFileRoutingModule],
  declarations: [UploadFileUpdateComponent],
  entryComponents: [UploadFileUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class UploadFileModule {}
