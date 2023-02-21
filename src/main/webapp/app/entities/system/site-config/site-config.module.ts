import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SiteConfigUpdateComponent } from './update/site-config-update.component';
import { SiteConfigSharedModule } from './site-config-shared.module';
import { SiteConfigRoutingModule } from './route/site-config-routing.module';

@NgModule({
  imports: [SharedModule, SiteConfigSharedModule, SiteConfigRoutingModule],
  declarations: [SiteConfigUpdateComponent],
  entryComponents: [SiteConfigUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SiteConfigModule {}
