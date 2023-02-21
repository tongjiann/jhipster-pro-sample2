import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DataPermissionRuleUpdateComponent } from './update/data-permission-rule-update.component';
import { DataPermissionRuleSharedModule } from './data-permission-rule-shared.module';
import { DataPermissionRuleRoutingModule } from './route/data-permission-rule-routing.module';

@NgModule({
  imports: [SharedModule, DataPermissionRuleSharedModule, DataPermissionRuleRoutingModule],
  declarations: [DataPermissionRuleUpdateComponent],
  entryComponents: [DataPermissionRuleUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class DataPermissionRuleModule {}
