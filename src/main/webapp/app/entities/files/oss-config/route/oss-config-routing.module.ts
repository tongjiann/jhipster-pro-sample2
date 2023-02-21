import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OssConfigComponent } from '../list/oss-config.component';
import { OssConfigUpdateComponent } from '../update/oss-config-update.component';
import { OssConfigRoutingResolveService } from './oss-config-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const ossConfigRoute: Routes = [
  {
    path: '',
    component: OssConfigComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.filesOssConfig.home.title',
      entityClassName: 'OssConfig',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OssConfigUpdateComponent,
    resolve: {
      ossConfig: OssConfigRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesOssConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OssConfigUpdateComponent,
    resolve: {
      ossConfig: OssConfigRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesOssConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OssConfigUpdateComponent,
    resolve: {
      ossConfig: OssConfigRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesOssConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ossConfigRoute)],
  exports: [RouterModule],
})
export class OssConfigRoutingModule {}
