import { NgModule, CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LayoutProComponent } from '@brand';
import { LayoutModule } from 'app/layouts/layouts.module';

@NgModule({
  imports: [
    SharedModule,
    LayoutModule,
    RouterModule.forChild([
      {
        path: '',
        component: LayoutProComponent,
        data: {
          authorities: ['ROLE_ADMIN', 'ROLE_USER'],
        },
        canActivate: [UserRouteAccessService],
        children: [
          {
            path: 'oss-config',
            loadChildren: () => import('./oss-config/oss-config.module').then(m => m.OssConfigModule),
          },

          {
            path: 'upload-image',
            loadChildren: () => import('./upload-image/upload-image.module').then(m => m.UploadImageModule),
          },

          {
            path: 'sms-config',
            loadChildren: () => import('./sms-config/sms-config.module').then(m => m.SmsConfigModule),
          },

          {
            path: 'resource-category',
            loadChildren: () => import('./resource-category/resource-category.module').then(m => m.ResourceCategoryModule),
          },

          {
            path: 'upload-file',
            loadChildren: () => import('./upload-file/upload-file.module').then(m => m.UploadFileModule),
          },
          /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ],
      },
    ]),
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
})
export class FilesModule {}
