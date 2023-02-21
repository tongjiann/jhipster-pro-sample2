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
            path: 'u-report-file',
            loadChildren: () => import('./u-report-file/u-report-file.module').then(m => m.UReportFileModule),
          },
          /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ],
      },
    ]),
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
})
export class ReportModule {}
