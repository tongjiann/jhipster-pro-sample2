import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DepartmentAuthorityComponent } from '../list/department-authority.component';
import { DepartmentAuthorityUpdateComponent } from '../update/department-authority-update.component';
import { DepartmentAuthorityRoutingResolveService } from './department-authority-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const departmentAuthorityRoute: Routes = [
  {
    path: '',
    component: DepartmentAuthorityComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.settingsDepartmentAuthority.home.title',
      entityClassName: 'DepartmentAuthority',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepartmentAuthorityUpdateComponent,
    resolve: {
      departmentAuthority: DepartmentAuthorityRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsDepartmentAuthority.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepartmentAuthorityUpdateComponent,
    resolve: {
      departmentAuthority: DepartmentAuthorityRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsDepartmentAuthority.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepartmentAuthorityUpdateComponent,
    resolve: {
      departmentAuthority: DepartmentAuthorityRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsDepartmentAuthority.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(departmentAuthorityRoute)],
  exports: [RouterModule],
})
export class DepartmentAuthorityRoutingModule {}
