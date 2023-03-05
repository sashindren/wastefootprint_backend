import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepartmentsComponent } from './list/departments.component';
import { DepartmentsDetailComponent } from './detail/departments-detail.component';
import { DepartmentsUpdateComponent } from './update/departments-update.component';
import { DepartmentsDeleteDialogComponent } from './delete/departments-delete-dialog.component';
import { DepartmentsRoutingModule } from './route/departments-routing.module';

@NgModule({
  imports: [SharedModule, DepartmentsRoutingModule],
  declarations: [DepartmentsComponent, DepartmentsDetailComponent, DepartmentsUpdateComponent, DepartmentsDeleteDialogComponent],
})
export class DepartmentsModule {}
