import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SysFillRuleUpdateComponent } from './update/sys-fill-rule-update.component';
import { SysFillRuleSharedModule } from './sys-fill-rule-shared.module';
import { SysFillRuleRoutingModule } from './route/sys-fill-rule-routing.module';

@NgModule({
  imports: [SharedModule, SysFillRuleSharedModule, SysFillRuleRoutingModule],
  declarations: [SysFillRuleUpdateComponent],
  entryComponents: [SysFillRuleUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SysFillRuleModule {}
