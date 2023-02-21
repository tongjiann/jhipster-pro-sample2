import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SysLogComponent } from './list/sys-log.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [SysLogComponent],
  exports: [SysLogComponent],
  entryComponents: [SysLogComponent],
})
export class SysLogSharedModule {}
