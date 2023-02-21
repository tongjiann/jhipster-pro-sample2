import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AnnouncementRecordComponent } from './list/announcement-record.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [AnnouncementRecordComponent],
  exports: [AnnouncementRecordComponent],
  entryComponents: [AnnouncementRecordComponent],
})
export class AnnouncementRecordSharedModule {}
