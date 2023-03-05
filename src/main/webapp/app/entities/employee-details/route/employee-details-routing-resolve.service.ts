import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeeDetails } from '../employee-details.model';
import { EmployeeDetailsService } from '../service/employee-details.service';

@Injectable({ providedIn: 'root' })
export class EmployeeDetailsRoutingResolveService implements Resolve<IEmployeeDetails | null> {
  constructor(protected service: EmployeeDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeDetails | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeeDetails: HttpResponse<IEmployeeDetails>) => {
          if (employeeDetails.body) {
            return of(employeeDetails.body);
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
