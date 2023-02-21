import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResourceCategoryComponent } from '../list/resource-category.component';
import { ResourceCategoryUpdateComponent } from '../update/resource-category-update.component';
import { ResourceCategoryRoutingResolveService } from './resource-category-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const resourceCategoryRoute: Routes = [
  {
    path: '',
    component: ResourceCategoryComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.filesResourceCategory.home.title',
      entityClassName: 'ResourceCategory',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResourceCategoryUpdateComponent,
    resolve: {
      resourceCategory: ResourceCategoryRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesResourceCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResourceCategoryUpdateComponent,
    resolve: {
      resourceCategory: ResourceCategoryRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesResourceCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResourceCategoryUpdateComponent,
    resolve: {
      resourceCategory: ResourceCategoryRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesResourceCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(resourceCategoryRoute)],
  exports: [RouterModule],
})
export class ResourceCategoryRoutingModule {}
