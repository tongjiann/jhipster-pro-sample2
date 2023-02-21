import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ViewPermissionComponent } from './list/view-permission.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [ViewPermissionComponent],
  exports: [ViewPermissionComponent],
  entryComponents: [ViewPermissionComponent],
})
export class ViewPermissionSharedModule {}
