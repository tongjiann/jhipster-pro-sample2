import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DepartmentComponent } from '../list/department.component';
import { DepartmentUpdateComponent } from '../update/department-update.component';
import { DepartmentRoutingResolveService } from './department-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const departmentRoute: Routes = [
  {
    path: '',
    component: DepartmentComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.settingsDepartment.home.title',
      entityClassName: 'Department',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepartmentUpdateComponent,
    resolve: {
      department: DepartmentRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsDepartment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepartmentUpdateComponent,
    resolve: {
      department: DepartmentRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsDepartment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepartmentUpdateComponent,
    resolve: {
      department: DepartmentRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsDepartment.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(departmentRoute)],
  exports: [RouterModule],
})
export class DepartmentRoutingModule {}
