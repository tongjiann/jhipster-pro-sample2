import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AnnouncementRecordComponent } from '../list/announcement-record.component';
import { AnnouncementRecordUpdateComponent } from '../update/announcement-record-update.component';
import { AnnouncementRecordRoutingResolveService } from './announcement-record-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const announcementRecordRoute: Routes = [
  {
    path: '',
    component: AnnouncementRecordComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.announcementRecord.home.title',
      entityClassName: 'AnnouncementRecord',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnnouncementRecordUpdateComponent,
    resolve: {
      announcementRecord: AnnouncementRecordRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.announcementRecord.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnnouncementRecordUpdateComponent,
    resolve: {
      announcementRecord: AnnouncementRecordRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.announcementRecord.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnnouncementRecordUpdateComponent,
    resolve: {
      announcementRecord: AnnouncementRecordRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.announcementRecord.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(announcementRecordRoute)],
  exports: [RouterModule],
})
export class AnnouncementRecordRoutingModule {}
