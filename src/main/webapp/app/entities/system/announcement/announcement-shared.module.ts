import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AnnouncementComponent } from './list/announcement.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [AnnouncementComponent],
  exports: [AnnouncementComponent],
  entryComponents: [AnnouncementComponent],
})
export class AnnouncementSharedModule {}
