import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SiteConfigComponent } from './list/site-config.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [SiteConfigComponent],
  exports: [SiteConfigComponent],
  entryComponents: [SiteConfigComponent],
})
export class SiteConfigSharedModule {}
