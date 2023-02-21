import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SmsTemplateUpdateComponent } from './update/sms-template-update.component';
import { SmsTemplateSharedModule } from './sms-template-shared.module';
import { SmsTemplateRoutingModule } from './route/sms-template-routing.module';

@NgModule({
  imports: [SharedModule, SmsTemplateSharedModule, SmsTemplateRoutingModule],
  declarations: [SmsTemplateUpdateComponent],
  entryComponents: [SmsTemplateUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SmsTemplateModule {}
