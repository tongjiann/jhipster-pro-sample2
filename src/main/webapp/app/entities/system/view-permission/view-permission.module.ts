import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ViewPermissionUpdateComponent } from './update/view-permission-update.component';
import { ViewPermissionSharedModule } from './view-permission-shared.module';
import { ViewPermissionRoutingModule } from './route/view-permission-routing.module';

@NgModule({
  imports: [SharedModule, ViewPermissionSharedModule, ViewPermissionRoutingModule],
  declarations: [ViewPermissionUpdateComponent],
  entryComponents: [ViewPermissionUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ViewPermissionModule {}
