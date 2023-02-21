import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { RegionCodeUpdateComponent } from './update/region-code-update.component';
import { RegionCodeSharedModule } from './region-code-shared.module';
import { RegionCodeRoutingModule } from './route/region-code-routing.module';

@NgModule({
  imports: [SharedModule, RegionCodeSharedModule, RegionCodeRoutingModule],
  declarations: [RegionCodeUpdateComponent],
  entryComponents: [RegionCodeUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class RegionCodeModule {}
