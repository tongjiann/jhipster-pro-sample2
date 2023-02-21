import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DataDictionaryUpdateComponent } from './update/data-dictionary-update.component';
import { DataDictionarySharedModule } from './data-dictionary-shared.module';
import { DataDictionaryRoutingModule } from './route/data-dictionary-routing.module';

@NgModule({
  imports: [SharedModule, DataDictionarySharedModule, DataDictionaryRoutingModule],
  declarations: [DataDictionaryUpdateComponent],
  entryComponents: [DataDictionaryUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class DataDictionaryModule {}
