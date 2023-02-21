import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SmsConfigComponent } from '../list/sms-config.component';
import { SmsConfigUpdateComponent } from '../update/sms-config-update.component';
import { SmsConfigRoutingResolveService } from './sms-config-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const smsConfigRoute: Routes = [
  {
    path: '',
    component: SmsConfigComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.filesSmsConfig.home.title',
      entityClassName: 'SmsConfig',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SmsConfigUpdateComponent,
    resolve: {
      smsConfig: SmsConfigRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesSmsConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SmsConfigUpdateComponent,
    resolve: {
      smsConfig: SmsConfigRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesSmsConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SmsConfigUpdateComponent,
    resolve: {
      smsConfig: SmsConfigRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesSmsConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(smsConfigRoute)],
  exports: [RouterModule],
})
export class SmsConfigRoutingModule {}
