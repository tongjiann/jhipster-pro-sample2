import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BusinessTypeUpdateComponent } from './update/business-type-update.component';
import { BusinessTypeSharedModule } from './business-type-shared.module';
import { BusinessTypeRoutingModule } from './route/business-type-routing.module';

@NgModule({
  imports: [SharedModule, BusinessTypeSharedModule, BusinessTypeRoutingModule],
  declarations: [BusinessTypeUpdateComponent],
  entryComponents: [BusinessTypeUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BusinessTypeModule {}
