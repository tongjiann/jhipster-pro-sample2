import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ResourceCategoryComponent } from './list/resource-category.component';
import { SharedModule } from 'app/shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule],
  declarations: [ResourceCategoryComponent],
  exports: [ResourceCategoryComponent],
  entryComponents: [ResourceCategoryComponent],
})
export class ResourceCategorySharedModule {}
