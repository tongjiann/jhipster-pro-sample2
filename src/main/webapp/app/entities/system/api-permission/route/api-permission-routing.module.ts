import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApiPermissionComponent } from '../list/api-permission.component';
import { ApiPermissionUpdateComponent } from '../update/api-permission-update.component';
import { ApiPermissionRoutingResolveService } from './api-permission-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const apiPermissionRoute: Routes = [
  {
    path: '',
    component: ApiPermissionComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.systemApiPermission.home.title',
      entityClassName: 'ApiPermission',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApiPermissionUpdateComponent,
    resolve: {
      apiPermission: ApiPermissionRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.systemApiPermission.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApiPermissionUpdateComponent,
    resolve: {
      apiPermission: ApiPermissionRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.systemApiPermission.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApiPermissionUpdateComponent,
    resolve: {
      apiPermission: ApiPermissionRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.systemApiPermission.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(apiPermissionRoute)],
  exports: [RouterModule],
})
export class ApiPermissionRoutingModule {}
