import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PositionUpdateComponent } from './update/position-update.component';
import { PositionSharedModule } from './position-shared.module';
import { PositionRoutingModule } from './route/position-routing.module';

@NgModule({
  imports: [SharedModule, PositionSharedModule, PositionRoutingModule],
  declarations: [PositionUpdateComponent],
  entryComponents: [PositionUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class PositionModule {}
