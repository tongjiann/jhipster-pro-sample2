import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SmsMessageComponent } from './list/sms-message.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [SmsMessageComponent],
  exports: [SmsMessageComponent],
  entryComponents: [SmsMessageComponent],
})
export class SmsMessageSharedModule {}
