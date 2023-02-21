import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UploadImageComponent } from '../list/upload-image.component';
import { UploadImageUpdateComponent } from '../update/upload-image-update.component';
import { UploadImageRoutingResolveService } from './upload-image-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const uploadImageRoute: Routes = [
  {
    path: '',
    component: UploadImageComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.filesUploadImage.home.title',
      entityClassName: 'UploadImage',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UploadImageUpdateComponent,
    resolve: {
      uploadImage: UploadImageRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesUploadImage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UploadImageUpdateComponent,
    resolve: {
      uploadImage: UploadImageRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesUploadImage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UploadImageUpdateComponent,
    resolve: {
      uploadImage: UploadImageRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.filesUploadImage.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(uploadImageRoute)],
  exports: [RouterModule],
})
export class UploadImageRoutingModule {}
