import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { RegionCodeComponent } from './list/region-code.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [RegionCodeComponent],
  exports: [RegionCodeComponent],
  entryComponents: [RegionCodeComponent],
})
export class RegionCodeSharedModule {}
