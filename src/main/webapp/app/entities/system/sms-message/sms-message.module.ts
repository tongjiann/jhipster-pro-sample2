import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SmsMessageUpdateComponent } from './update/sms-message-update.component';
import { SmsMessageSharedModule } from './sms-message-shared.module';
import { SmsMessageRoutingModule } from './route/sms-message-routing.module';

@NgModule({
  imports: [SharedModule, SmsMessageSharedModule, SmsMessageRoutingModule],
  declarations: [SmsMessageUpdateComponent],
  entryComponents: [SmsMessageUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SmsMessageModule {}
