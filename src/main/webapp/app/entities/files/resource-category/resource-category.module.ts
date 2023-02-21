import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ResourceCategoryUpdateComponent } from './update/resource-category-update.component';
import { ResourceCategorySharedModule } from './resource-category-shared.module';
import { ResourceCategoryRoutingModule } from './route/resource-category-routing.module';

@NgModule({
  imports: [SharedModule, ResourceCategorySharedModule, ResourceCategoryRoutingModule],
  declarations: [ResourceCategoryUpdateComponent],
  entryComponents: [ResourceCategoryUpdateComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ResourceCategoryModule {}
