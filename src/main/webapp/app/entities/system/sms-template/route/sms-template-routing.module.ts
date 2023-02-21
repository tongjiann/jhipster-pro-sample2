import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SmsTemplateComponent } from '../list/sms-template.component';
import { SmsTemplateUpdateComponent } from '../update/sms-template-update.component';
import { SmsTemplateRoutingResolveService } from './sms-template-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const smsTemplateRoute: Routes = [
  {
    path: '',
    component: SmsTemplateComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.smsTemplate.home.title',
      entityClassName: 'SmsTemplate',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SmsTemplateUpdateComponent,
    resolve: {
      smsTemplate: SmsTemplateRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.smsTemplate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SmsTemplateUpdateComponent,
    resolve: {
      smsTemplate: SmsTemplateRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.smsTemplate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SmsTemplateUpdateComponent,
    resolve: {
      smsTemplate: SmsTemplateRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.smsTemplate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(smsTemplateRoute)],
  exports: [RouterModule],
})
export class SmsTemplateRoutingModule {}
