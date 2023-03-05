import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductionWaste } from '../production-waste.model';
import { ProductionWasteService } from '../service/production-waste.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './production-waste-delete-dialog.component.html',
})
export class ProductionWasteDeleteDialogComponent {
  productionWaste?: IProductionWaste;

  constructor(protected productionWasteService: ProductionWasteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productionWasteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
