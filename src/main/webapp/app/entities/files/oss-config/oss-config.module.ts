import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { OssConfigUpdateComponent } from './update/oss-config-update.component';
import { OssConfigSharedModule } from './oss-config-shared.module';
import { OssConfigRoutingModule } from './route/oss-config-routing.module';

@NgModule({
  imports: [SharedModule, OssConfigSharedModule, OssConfigRoutingModule],
  declarations: [OssConfigUpdateComponent],
  entryComponents: [OssConfigUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class OssConfigModule {}
