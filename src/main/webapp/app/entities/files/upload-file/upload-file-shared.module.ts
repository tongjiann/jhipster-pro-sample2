import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UploadFileComponent } from './list/upload-file.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [UploadFileComponent],
  exports: [UploadFileComponent],
  entryComponents: [UploadFileComponent],
})
export class UploadFileSharedModule {}
