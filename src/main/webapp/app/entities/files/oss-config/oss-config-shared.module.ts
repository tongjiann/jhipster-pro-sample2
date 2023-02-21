import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { OssConfigComponent } from './list/oss-config.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [OssConfigComponent],
  exports: [OssConfigComponent],
  entryComponents: [OssConfigComponent],
})
export class OssConfigSharedModule {}
