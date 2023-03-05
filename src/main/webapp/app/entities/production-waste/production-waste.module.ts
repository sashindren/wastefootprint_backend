import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductionWasteComponent } from './list/production-waste.component';
import { ProductionWasteDetailComponent } from './detail/production-waste-detail.component';
import { ProductionWasteUpdateComponent } from './update/production-waste-update.component';
import { ProductionWasteDeleteDialogComponent } from './delete/production-waste-delete-dialog.component';
import { ProductionWasteRoutingModule } from './route/production-waste-routing.module';

@NgModule({
  imports: [SharedModule, ProductionWasteRoutingModule],
  declarations: [
    ProductionWasteComponent,
    ProductionWasteDetailComponent,
    ProductionWasteUpdateComponent,
    ProductionWasteDeleteDialogComponent,
  ],
})
export class ProductionWasteModule {}
