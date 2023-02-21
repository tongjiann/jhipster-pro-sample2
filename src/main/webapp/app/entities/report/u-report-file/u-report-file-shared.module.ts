import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UReportFileComponent } from './list/u-report-file.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [UReportFileComponent],
  exports: [UReportFileComponent],
  entryComponents: [UReportFileComponent],
})
export class UReportFileSharedModule {}
