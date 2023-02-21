import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BusinessTypeComponent } from './list/business-type.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [BusinessTypeComponent],
  exports: [BusinessTypeComponent],
  entryComponents: [BusinessTypeComponent],
})
export class BusinessTypeSharedModule {}
