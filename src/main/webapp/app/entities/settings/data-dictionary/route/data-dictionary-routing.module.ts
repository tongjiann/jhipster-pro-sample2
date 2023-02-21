import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DataDictionaryComponent } from '../list/data-dictionary.component';
import { DataDictionaryUpdateComponent } from '../update/data-dictionary-update.component';
import { DataDictionaryRoutingResolveService } from './data-dictionary-routing-resolve.service';
import { CommonTableDataResolve } from 'app/shared/util/common-table-data-resolve';

const dataDictionaryRoute: Routes = [
  {
    path: '',
    component: DataDictionaryComponent,
    resolve: {
      commonTableData: CommonTableDataResolve,
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.settingsDataDictionary.home.title',
      entityClassName: 'DataDictionary',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DataDictionaryUpdateComponent,
    resolve: {
      dataDictionary: DataDictionaryRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsDataDictionary.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DataDictionaryUpdateComponent,
    resolve: {
      dataDictionary: DataDictionaryRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsDataDictionary.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DataDictionaryUpdateComponent,
    resolve: {
      dataDictionary: DataDictionaryRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.settingsDataDictionary.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dataDictionaryRoute)],
  exports: [RouterModule],
})
export class DataDictionaryRoutingModule {}
