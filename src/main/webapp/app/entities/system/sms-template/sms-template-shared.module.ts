import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SmsTemplateComponent } from './list/sms-template.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [SmsTemplateComponent],
  exports: [SmsTemplateComponent],
  entryComponents: [SmsTemplateComponent],
})
export class SmsTemplateSharedModule {}
