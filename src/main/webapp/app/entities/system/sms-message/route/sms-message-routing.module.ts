import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SmsMessageComponent } from '../list/sms-message.component';
import { SmsMessageUpdateComponent } from '../update/sms-message-update.component';
import { SmsMessageRoutingResolveService } from './sms-message-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const smsMessageRoute: Routes = [
  {
    path: '',
    component: SmsMessageComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.smsMessage.home.title',
      entityClassName: 'SmsMessage',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SmsMessageUpdateComponent,
    resolve: {
      smsMessage: SmsMessageRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.smsMessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SmsMessageUpdateComponent,
    resolve: {
      smsMessage: SmsMessageRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.smsMessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SmsMessageUpdateComponent,
    resolve: {
      smsMessage: SmsMessageRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.smsMessage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(smsMessageRoute)],
  exports: [RouterModule],
})
export class SmsMessageRoutingModule {}
