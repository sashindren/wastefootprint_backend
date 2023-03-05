import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProductionWasteFormService, ProductionWasteFormGroup } from './production-waste-form.service';
import { IProductionWaste } from '../production-waste.model';
import { ProductionWasteService } from '../service/production-waste.service';
import { IDepartments } from 'app/entities/departments/departments.model';
import { DepartmentsService } from 'app/entities/departments/service/departments.service';

@Component({
  selector: 'jhi-production-waste-update',
  templateUrl: './production-waste-update.component.html',
})
export class ProductionWasteUpdateComponent implements OnInit {
  isSaving = false;
  productionWaste: IProductionWaste | null = null;

  departmentsSharedCollection: IDepartments[] = [];

  editForm: ProductionWasteFormGroup = this.productionWasteFormService.createProductionWasteFormGroup();

  constructor(
    protected productionWasteService: ProductionWasteService,
    protected productionWasteFormService: ProductionWasteFormService,
    protected departmentsService: DepartmentsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDepartments = (o1: IDepartments | null, o2: IDepartments | null): boolean => this.departmentsService.compareDepartments(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productionWaste }) => {
      this.productionWaste = productionWaste;
      if (productionWaste) {
        this.updateForm(productionWaste);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productionWaste = this.productionWasteFormService.getProductionWaste(this.editForm);
    if (productionWaste.id !== null) {
      this.subscribeToSaveResponse(this.productionWasteService.update(productionWaste));
    } else {
      this.subscribeToSaveResponse(this.productionWasteService.create(productionWaste));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductionWaste>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(productionWaste: IProductionWaste): void {
    this.productionWaste = productionWaste;
    this.productionWasteFormService.resetForm(this.editForm, productionWaste);

    this.departmentsSharedCollection = this.departmentsService.addDepartmentsToCollectionIfMissing<IDepartments>(
      this.departmentsSharedCollection,
      productionWaste.department
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departmentsService
      .query()
      .pipe(map((res: HttpResponse<IDepartments[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartments[]) =>
          this.departmentsService.addDepartmentsToCollectionIfMissing<IDepartments>(departments, this.productionWaste?.department)
        )
      )
      .subscribe((departments: IDepartments[]) => (this.departmentsSharedCollection = departments));
  }
}
