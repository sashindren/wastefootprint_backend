import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDepartments } from '../departments.model';

@Component({
  selector: 'jhi-departments-detail',
  templateUrl: './departments-detail.component.html',
})
export class DepartmentsDetailComponent implements OnInit {
  departments: IDepartments | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ departments }) => {
      this.departments = departments;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
