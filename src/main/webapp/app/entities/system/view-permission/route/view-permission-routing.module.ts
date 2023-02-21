import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ViewPermissionComponent } from '../list/view-permission.component';
import { ViewPermissionUpdateComponent } from '../update/view-permission-update.component';
import { ViewPermissionRoutingResolveService } from './view-permission-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const viewPermissionRoute: Routes = [
  {
    path: '',
    component: ViewPermissionComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.systemViewPermission.home.title',
      entityClassName: 'ViewPermission',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ViewPermissionUpdateComponent,
    resolve: {
      viewPermission: ViewPermissionRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.systemViewPermission.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ViewPermissionUpdateComponent,
    resolve: {
      viewPermission: ViewPermissionRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.systemViewPermission.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ViewPermissionUpdateComponent,
    resolve: {
      viewPermission: ViewPermissionRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.systemViewPermission.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(viewPermissionRoute)],
  exports: [RouterModule],
})
export class ViewPermissionRoutingModule {}
