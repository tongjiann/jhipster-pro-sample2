import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RegionCodeComponent } from '../list/region-code.component';
import { RegionCodeUpdateComponent } from '../update/region-code-update.component';
import { RegionCodeRoutingResolveService } from './region-code-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const regionCodeRoute: Routes = [
  {
    path: '',
    component: RegionCodeComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.settingsRegionCode.home.title',
      entityClassName: 'RegionCode',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RegionCodeUpdateComponent,
    resolve: {
      regionCode: RegionCodeRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsRegionCode.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RegionCodeUpdateComponent,
    resolve: {
      regionCode: RegionCodeRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsRegionCode.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RegionCodeUpdateComponent,
    resolve: {
      regionCode: RegionCodeRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsRegionCode.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(regionCodeRoute)],
  exports: [RouterModule],
})
export class RegionCodeRoutingModule {}
