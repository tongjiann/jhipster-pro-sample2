import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DataPermissionRuleComponent } from './list/data-permission-rule.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [DataPermissionRuleComponent],
  exports: [DataPermissionRuleComponent],
  entryComponents: [DataPermissionRuleComponent],
})
export class DataPermissionRuleSharedModule {}
