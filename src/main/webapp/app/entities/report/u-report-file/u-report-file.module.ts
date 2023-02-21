import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UReportFileUpdateComponent } from './update/u-report-file-update.component';
import { UReportFileSharedModule } from './u-report-file-shared.module';
import { UReportFileRoutingModule } from './route/u-report-file-routing.module';

@NgModule({
  imports: [SharedModule, UReportFileSharedModule, UReportFileRoutingModule],
  declarations: [UReportFileUpdateComponent],
  entryComponents: [UReportFileUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class UReportFileModule {}
