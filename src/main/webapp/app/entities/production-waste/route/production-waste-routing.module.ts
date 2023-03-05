import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductionWasteComponent } from '../list/production-waste.component';
import { ProductionWasteDetailComponent } from '../detail/production-waste-detail.component';
import { ProductionWasteUpdateComponent } from '../update/production-waste-update.component';
import { ProductionWasteRoutingResolveService } from './production-waste-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const productionWasteRoute: Routes = [
  {
    path: '',
    component: ProductionWasteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductionWasteDetailComponent,
    resolve: {
      productionWaste: ProductionWasteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductionWasteUpdateComponent,
    resolve: {
      productionWaste: ProductionWasteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductionWasteUpdateComponent,
    resolve: {
      productionWaste: ProductionWasteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productionWasteRoute)],
  exports: [RouterModule],
})
export class ProductionWasteRoutingModule {}
