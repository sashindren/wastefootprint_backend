import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductionWaste } from '../production-waste.model';
import { ProductionWasteService } from '../service/production-waste.service';

@Injectable({ providedIn: 'root' })
export class ProductionWasteRoutingResolveService implements Resolve<IProductionWaste | null> {
  constructor(protected service: ProductionWasteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductionWaste | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productionWaste: HttpResponse<IProductionWaste>) => {
          if (productionWaste.body) {
            return of(productionWaste.body);
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
