import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DepartmentUpdateComponent } from './update/department-update.component';
import { DepartmentSharedModule } from './department-shared.module';
import { DepartmentRoutingModule } from './route/department-routing.module';

@NgModule({
  imports: [SharedModule, DepartmentSharedModule, DepartmentRoutingModule],
  declarations: [DepartmentUpdateComponent],
  entryComponents: [DepartmentUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class DepartmentModule {}
