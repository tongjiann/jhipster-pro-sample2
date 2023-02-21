import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SmsConfigComponent } from './list/sms-config.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [SmsConfigComponent],
  exports: [SmsConfigComponent],
  entryComponents: [SmsConfigComponent],
})
export class SmsConfigSharedModule {}
