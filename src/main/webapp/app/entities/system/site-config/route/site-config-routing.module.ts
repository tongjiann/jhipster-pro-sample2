import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SiteConfigComponent } from '../list/site-config.component';
import { SiteConfigUpdateComponent } from '../update/site-config-update.component';
import { SiteConfigRoutingResolveService } from './site-config-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const siteConfigRoute: Routes = [
  {
    path: '',
    component: SiteConfigComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.siteConfig.home.title',
      entityClassName: 'SiteConfig',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SiteConfigUpdateComponent,
    resolve: {
      siteConfig: SiteConfigRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.siteConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SiteConfigUpdateComponent,
    resolve: {
      siteConfig: SiteConfigRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.siteConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SiteConfigUpdateComponent,
    resolve: {
      siteConfig: SiteConfigRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.siteConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(siteConfigRoute)],
  exports: [RouterModule],
})
export class SiteConfigRoutingModule {}
