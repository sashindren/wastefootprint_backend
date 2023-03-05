import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DepartmentsFormService, DepartmentsFormGroup } from './departments-form.service';
import { IDepartments } from '../departments.model';
import { DepartmentsService } from '../service/departments.service';

@Component({
  selector: 'jhi-departments-update',
  templateUrl: './departments-update.component.html',
})
export class DepartmentsUpdateComponent implements OnInit {
  isSaving = false;
  departments: IDepartments | null = null;

  editForm: DepartmentsFormGroup = this.departmentsFormService.createDepartmentsFormGroup();

  constructor(
    protected departmentsService: DepartmentsService,
    protected departmentsFormService: DepartmentsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ departments }) => {
      this.departments = departments;
      if (departments) {
        this.updateForm(departments);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const departments = this.departmentsFormService.getDepartments(this.editForm);
    if (departments.id !== null) {
      this.subscribeToSaveResponse(this.departmentsService.update(departments));
    } else {
      this.subscribeToSaveResponse(this.departmentsService.create(departments));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartments>>): void {
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

  protected updateForm(departments: IDepartments): void {
    this.departments = departments;
    this.departmentsFormService.resetForm(this.editForm, departments);
  }
}
