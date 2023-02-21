import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ApiPermissionUpdateComponent } from './update/api-permission-update.component';
import { ApiPermissionSharedModule } from './api-permission-shared.module';
import { ApiPermissionRoutingModule } from './route/api-permission-routing.module';

@NgModule({
  imports: [SharedModule, ApiPermissionSharedModule, ApiPermissionRoutingModule],
  declarations: [ApiPermissionUpdateComponent],
  entryComponents: [ApiPermissionUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ApiPermissionModule {}
