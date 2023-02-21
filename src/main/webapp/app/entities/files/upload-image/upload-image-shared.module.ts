import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UploadImageComponent } from './list/upload-image.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [UploadImageComponent],
  exports: [UploadImageComponent],
  entryComponents: [UploadImageComponent],
})
export class UploadImageSharedModule {}
