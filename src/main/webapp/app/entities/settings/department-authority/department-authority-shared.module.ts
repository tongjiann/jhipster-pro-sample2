import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DepartmentAuthorityComponent } from './list/department-authority.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [DepartmentAuthorityComponent],
  exports: [DepartmentAuthorityComponent],
  entryComponents: [DepartmentAuthorityComponent],
})
export class DepartmentAuthoritySharedModule {}
