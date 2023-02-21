import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ApiPermissionComponent } from './list/api-permission.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [ApiPermissionComponent],
  exports: [ApiPermissionComponent],
  entryComponents: [ApiPermissionComponent],
})
export class ApiPermissionSharedModule {}
