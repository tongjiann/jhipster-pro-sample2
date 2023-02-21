import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SysFillRuleComponent } from './list/sys-fill-rule.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [SysFillRuleComponent],
  exports: [SysFillRuleComponent],
  entryComponents: [SysFillRuleComponent],
})
export class SysFillRuleSharedModule {}
