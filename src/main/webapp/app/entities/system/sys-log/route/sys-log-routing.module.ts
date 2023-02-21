import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SysLogComponent } from '../list/sys-log.component';
import { SysLogUpdateComponent } from '../update/sys-log-update.component';
import { SysLogRoutingResolveService } from './sys-log-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const sysLogRoute: Routes = [
  {
    path: '',
    component: SysLogComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.sysLog.home.title',
      entityClassName: 'SysLog',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SysLogUpdateComponent,
    resolve: {
      sysLog: SysLogRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.sysLog.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SysLogUpdateComponent,
    resolve: {
      sysLog: SysLogRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.sysLog.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SysLogUpdateComponent,
    resolve: {
      sysLog: SysLogRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.sysLog.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sysLogRoute)],
  exports: [RouterModule],
})
export class SysLogRoutingModule {}
