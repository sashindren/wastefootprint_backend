import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDepartments } from '../departments.model';
import { DepartmentsService } from '../service/departments.service';

@Injectable({ providedIn: 'root' })
export class DepartmentsRoutingResolveService implements Resolve<IDepartments | null> {
  constructor(protected service: DepartmentsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDepartments | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((departments: HttpResponse<IDepartments>) => {
          if (departments.body) {
            return of(departments.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
