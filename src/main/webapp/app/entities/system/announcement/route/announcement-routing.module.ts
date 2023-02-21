import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AnnouncementComponent } from '../list/announcement.component';
import { AnnouncementUpdateComponent } from '../update/announcement-update.component';
import { AnnouncementRoutingResolveService } from './announcement-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const announcementRoute: Routes = [
  {
    path: '',
    component: AnnouncementComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.announcement.home.title',
      entityClassName: 'Announcement',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnnouncementUpdateComponent,
    resolve: {
      announcement: AnnouncementRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.announcement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnnouncementUpdateComponent,
    resolve: {
      announcement: AnnouncementRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.announcement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnnouncementUpdateComponent,
    resolve: {
      announcement: AnnouncementRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.announcement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(announcementRoute)],
  exports: [RouterModule],
})
export class AnnouncementRoutingModule {}
