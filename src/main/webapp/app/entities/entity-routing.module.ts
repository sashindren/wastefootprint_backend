import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'employee-details',
        data: { pageTitle: 'EmployeeDetails' },
        loadChildren: () => import('./employee-details/employee-details.module').then(m => m.EmployeeDetailsModule),
      },
      {
        path: 'departments',
        data: { pageTitle: 'Departments' },
        loadChildren: () => import('./departments/departments.module').then(m => m.DepartmentsModule),
      },
      {
        path: 'production-waste',
        data: { pageTitle: 'ProductionWastes' },
        loadChildren: () => import('./production-waste/production-waste.module').then(m => m.ProductionWasteModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
