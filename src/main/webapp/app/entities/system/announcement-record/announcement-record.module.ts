import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AnnouncementRecordUpdateComponent } from './update/announcement-record-update.component';
import { AnnouncementRecordSharedModule } from './announcement-record-shared.module';
import { AnnouncementRecordRoutingModule } from './route/announcement-record-routing.module';

@NgModule({
  imports: [SharedModule, AnnouncementRecordSharedModule, AnnouncementRecordRoutingModule],
  declarations: [AnnouncementRecordUpdateComponent],
  entryComponents: [AnnouncementRecordUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AnnouncementRecordModule {}
