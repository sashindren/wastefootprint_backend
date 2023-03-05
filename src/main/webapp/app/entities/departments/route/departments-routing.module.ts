import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DepartmentsComponent } from '../list/departments.component';
import { DepartmentsDetailComponent } from '../detail/departments-detail.component';
import { DepartmentsUpdateComponent } from '../update/departments-update.component';
import { DepartmentsRoutingResolveService } from './departments-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const departmentsRoute: Routes = [
  {
    path: '',
    component: DepartmentsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepartmentsDetailComponent,
    resolve: {
      departments: DepartmentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepartmentsUpdateComponent,
    resolve: {
      departments: DepartmentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepartmentsUpdateComponent,
    resolve: {
      departments: DepartmentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(departmentsRoute)],
  exports: [RouterModule],
})
export class DepartmentsRoutingModule {}
