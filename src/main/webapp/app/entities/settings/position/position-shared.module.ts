import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PositionComponent } from './list/position.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [PositionComponent],
  exports: [PositionComponent],
  entryComponents: [PositionComponent],
})
export class PositionSharedModule {}
