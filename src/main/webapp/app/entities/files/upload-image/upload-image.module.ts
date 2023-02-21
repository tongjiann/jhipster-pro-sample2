import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UploadImageUpdateComponent } from './update/upload-image-update.component';
import { UploadImageSharedModule } from './upload-image-shared.module';
import { UploadImageRoutingModule } from './route/upload-image-routing.module';

@NgModule({
  imports: [SharedModule, UploadImageSharedModule, UploadImageRoutingModule],
  declarations: [UploadImageUpdateComponent],
  entryComponents: [UploadImageUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class UploadImageModule {}
