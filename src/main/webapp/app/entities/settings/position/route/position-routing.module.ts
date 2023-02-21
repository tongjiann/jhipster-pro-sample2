import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PositionComponent } from '../list/position.component';
import { PositionUpdateComponent } from '../update/position-update.component';
import { PositionRoutingResolveService } from './position-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const positionRoute: Routes = [
  {
    path: '',
    component: PositionComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.settingsPosition.home.title',
      entityClassName: 'Position',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PositionUpdateComponent,
    resolve: {
      position: PositionRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsPosition.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PositionUpdateComponent,
    resolve: {
      position: PositionRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsPosition.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PositionUpdateComponent,
    resolve: {
      position: PositionRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsPosition.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(positionRoute)],
  exports: [RouterModule],
})
export class PositionRoutingModule {}
