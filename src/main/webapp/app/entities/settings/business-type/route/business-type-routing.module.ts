import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BusinessTypeComponent } from '../list/business-type.component';
import { BusinessTypeUpdateComponent } from '../update/business-type-update.component';
import { BusinessTypeRoutingResolveService } from './business-type-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const businessTypeRoute: Routes = [
  {
    path: '',
    component: BusinessTypeComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.settingsBusinessType.home.title',
      entityClassName: 'BusinessType',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusinessTypeUpdateComponent,
    resolve: {
      businessType: BusinessTypeRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsBusinessType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusinessTypeUpdateComponent,
    resolve: {
      businessType: BusinessTypeRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsBusinessType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BusinessTypeUpdateComponent,
    resolve: {
      businessType: BusinessTypeRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsBusinessType.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(businessTypeRoute)],
  exports: [RouterModule],
})
export class BusinessTypeRoutingModule {}
