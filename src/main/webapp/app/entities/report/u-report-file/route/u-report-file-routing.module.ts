import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UReportFileComponent } from '../list/u-report-file.component';
import { UReportFileUpdateComponent } from '../update/u-report-file-update.component';
import { UReportFileRoutingResolveService } from './u-report-file-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const uReportFileRoute: Routes = [
  {
    path: '',
    component: UReportFileComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.reportUReportFile.home.title',
      entityClassName: 'UReportFile',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UReportFileUpdateComponent,
    resolve: {
      uReportFile: UReportFileRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.reportUReportFile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UReportFileUpdateComponent,
    resolve: {
      uReportFile: UReportFileRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.reportUReportFile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UReportFileUpdateComponent,
    resolve: {
      uReportFile: UReportFileRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.reportUReportFile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(uReportFileRoute)],
  exports: [RouterModule],
})
export class UReportFileRoutingModule {}
