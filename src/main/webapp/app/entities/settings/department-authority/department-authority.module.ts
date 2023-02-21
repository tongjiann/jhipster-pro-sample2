import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DepartmentAuthorityUpdateComponent } from './update/department-authority-update.component';
import { DepartmentAuthoritySharedModule } from './department-authority-shared.module';
import { DepartmentAuthorityRoutingModule } from './route/department-authority-routing.module';

@NgModule({
  imports: [SharedModule, DepartmentAuthoritySharedModule, DepartmentAuthorityRoutingModule],
  declarations: [DepartmentAuthorityUpdateComponent],
  entryComponents: [DepartmentAuthorityUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class DepartmentAuthorityModule {}
