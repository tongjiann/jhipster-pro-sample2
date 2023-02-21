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
            path: 'department-authority',
            loadChildren: () => import('./department-authority/department-authority.module').then(m => m.DepartmentAuthorityModule),
          },

          {
            path: 'department',
            loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
          },

          {
            path: 'business-type',
            loadChildren: () => import('./business-type/business-type.module').then(m => m.BusinessTypeModule),
          },

          {
            path: 'sys-fill-rule',
            loadChildren: () => import('./sys-fill-rule/sys-fill-rule.module').then(m => m.SysFillRuleModule),
          },

          {
            path: 'position',
            loadChildren: () => import('./position/position.module').then(m => m.PositionModule),
          },

          {
            path: 'data-dictionary',
            loadChildren: () => import('./data-dictionary/data-dictionary.module').then(m => m.DataDictionaryModule),
          },

          {
            path: 'region-code',
            loadChildren: () => import('./region-code/region-code.module').then(m => m.RegionCodeModule),
          },
          /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ],
      },
    ]),
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
})
export class SettingsModule {}
