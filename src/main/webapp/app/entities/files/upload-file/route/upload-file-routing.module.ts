import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UploadFileComponent } from '../list/upload-file.component';
import { UploadFileUpdateComponent } from '../update/upload-file-update.component';
import { UploadFileRoutingResolveService } from './upload-file-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const uploadFileRoute: Routes = [
  {
    path: '',
    component: UploadFileComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.filesUploadFile.home.title',
      entityClassName: 'UploadFile',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UploadFileUpdateComponent,
    resolve: {
      uploadFile: UploadFileRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesUploadFile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UploadFileUpdateComponent,
    resolve: {
      uploadFile: UploadFileRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesUploadFile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UploadFileUpdateComponent,
    resolve: {
      uploadFile: UploadFileRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesUploadFile.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(uploadFileRoute)],
  exports: [RouterModule],
})
export class UploadFileRoutingModule {}
