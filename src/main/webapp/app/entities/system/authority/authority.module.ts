import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AuthorityUpdateComponent } from './update/authority-update.component';
import { AuthoritySharedModule } from './authority-shared.module';
import { AuthorityRoutingModule } from './route/authority-routing.module';

@NgModule({
  imports: [SharedModule, AuthoritySharedModule, AuthorityRoutingModule],
  declarations: [AuthorityUpdateComponent],
  entryComponents: [AuthorityUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AuthorityModule {}
