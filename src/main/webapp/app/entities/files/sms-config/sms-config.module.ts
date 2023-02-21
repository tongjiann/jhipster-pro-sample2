import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SmsConfigUpdateComponent } from './update/sms-config-update.component';
import { SmsConfigSharedModule } from './sms-config-shared.module';
import { SmsConfigRoutingModule } from './route/sms-config-routing.module';

@NgModule({
  imports: [SharedModule, SmsConfigSharedModule, SmsConfigRoutingModule],
  declarations: [SmsConfigUpdateComponent],
  entryComponents: [SmsConfigUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SmsConfigModule {}
