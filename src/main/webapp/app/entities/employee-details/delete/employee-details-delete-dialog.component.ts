import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployeeDetails } from '../employee-details.model';
import { EmployeeDetailsService } from '../service/employee-details.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './employee-details-delete-dialog.component.html',
})
export class EmployeeDetailsDeleteDialogComponent {
  employeeDetails?: IEmployeeDetails;

  constructor(protected employeeDetailsService: EmployeeDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
