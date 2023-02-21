import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DataDictionaryComponent } from './list/data-dictionary.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [DataDictionaryComponent],
  exports: [DataDictionaryComponent],
  entryComponents: [DataDictionaryComponent],
})
export class DataDictionarySharedModule {}
