import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DepartmentComponent } from './list/department.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [DepartmentComponent],
  exports: [DepartmentComponent],
  entryComponents: [DepartmentComponent],
})
export class DepartmentSharedModule {}
