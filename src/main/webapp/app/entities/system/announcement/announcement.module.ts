import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AnnouncementUpdateComponent } from './update/announcement-update.component';
import { AnnouncementSharedModule } from './announcement-shared.module';
import { AnnouncementRoutingModule } from './route/announcement-routing.module';

@NgModule({
  imports: [SharedModule, AnnouncementSharedModule, AnnouncementRoutingModule],
  declarations: [AnnouncementUpdateComponent],
  entryComponents: [AnnouncementUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AnnouncementModule {}
