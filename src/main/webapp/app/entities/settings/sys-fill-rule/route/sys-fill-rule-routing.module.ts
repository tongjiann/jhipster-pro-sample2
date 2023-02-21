import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SysFillRuleComponent } from '../list/sys-fill-rule.component';
import { SysFillRuleUpdateComponent } from '../update/sys-fill-rule-update.component';
import { SysFillRuleRoutingResolveService } from './sys-fill-rule-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const sysFillRuleRoute: Routes = [
  {
    path: '',
    component: SysFillRuleComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.settingsSysFillRule.home.title',
      entityClassName: 'SysFillRule',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SysFillRuleUpdateComponent,
    resolve: {
      sysFillRule: SysFillRuleRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsSysFillRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SysFillRuleUpdateComponent,
    resolve: {
      sysFillRule: SysFillRuleRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsSysFillRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SysFillRuleUpdateComponent,
    resolve: {
      sysFillRule: SysFillRuleRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsSysFillRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sysFillRuleRoute)],
  exports: [RouterModule],
})
export class SysFillRuleRoutingModule {}
