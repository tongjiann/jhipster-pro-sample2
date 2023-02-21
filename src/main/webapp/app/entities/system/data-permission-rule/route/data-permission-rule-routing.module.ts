import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DataPermissionRuleComponent } from '../list/data-permission-rule.component';
import { DataPermissionRuleUpdateComponent } from '../update/data-permission-rule-update.component';
import { DataPermissionRuleRoutingResolveService } from './data-permission-rule-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const dataPermissionRuleRoute: Routes = [
  {
    path: '',
    component: DataPermissionRuleComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.dataPermissionRule.home.title',
      entityClassName: 'DataPermissionRule',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DataPermissionRuleUpdateComponent,
    resolve: {
      dataPermissionRule: DataPermissionRuleRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.dataPermissionRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DataPermissionRuleUpdateComponent,
    resolve: {
      dataPermissionRule: DataPermissionRuleRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.dataPermissionRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DataPermissionRuleUpdateComponent,
    resolve: {
      dataPermissionRule: DataPermissionRuleRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.dataPermissionRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dataPermissionRuleRoute)],
  exports: [RouterModule],
})
export class DataPermissionRuleRoutingModule {}
