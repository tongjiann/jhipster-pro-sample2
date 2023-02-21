import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SysLogUpdateComponent } from './update/sys-log-update.component';
import { SysLogSharedModule } from './sys-log-shared.module';
import { SysLogRoutingModule } from './route/sys-log-routing.module';

@NgModule({
  imports: [SharedModule, SysLogSharedModule, SysLogRoutingModule],
  declarations: [SysLogUpdateComponent],
  entryComponents: [SysLogUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SysLogModule {}
