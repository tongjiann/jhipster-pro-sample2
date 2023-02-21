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
          /* {
                        path: 'users',
                        loadChildren: () => import('./user-management/user-management.module').then(m => m.UserManagementModule)
                    } */

          {
            path: 'api-permission',
            loadChildren: () => import('./api-permission/api-permission.module').then(m => m.ApiPermissionModule),
          },

          {
            path: 'authority',
            loadChildren: () => import('./authority/authority.module').then(m => m.AuthorityModule),
          },

          {
            path: 'view-permission',
            loadChildren: () => import('./view-permission/view-permission.module').then(m => m.ViewPermissionModule),
          },

          {
            path: 'user',
            loadChildren: () => import('./user/user.module').then(m => m.UserModule),
          },
          /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ],
      },
    ]),
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
})
export class SystemModule {}
