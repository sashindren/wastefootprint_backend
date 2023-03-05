import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductionWaste, NewProductionWaste } from '../production-waste.model';

export type PartialUpdateProductionWaste = Partial<IProductionWaste> & Pick<IProductionWaste, 'id'>;

export type EntityResponseType = HttpResponse<IProductionWaste>;
export type EntityArrayResponseType = HttpResponse<IProductionWaste[]>;

@Injectable({ providedIn: 'root' })
export class ProductionWasteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/production-wastes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productionWaste: NewProductionWaste): Observable<EntityResponseType> {
    return this.http.post<IProductionWaste>(this.resourceUrl, productionWaste, { observe: 'response' });
  }

  update(productionWaste: IProductionWaste): Observable<EntityResponseType> {
    return this.http.put<IProductionWaste>(`${this.resourceUrl}/${this.getProductionWasteIdentifier(productionWaste)}`, productionWaste, {
      observe: 'response',
    });
  }

  partialUpdate(productionWaste: PartialUpdateProductionWaste): Observable<EntityResponseType> {
    return this.http.patch<IProductionWaste>(`${this.resourceUrl}/${this.getProductionWasteIdentifier(productionWaste)}`, productionWaste, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductionWaste>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductionWaste[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductionWasteIdentifier(productionWaste: Pick<IProductionWaste, 'id'>): number {
    return productionWaste.id;
  }

  compareProductionWaste(o1: Pick<IProductionWaste, 'id'> | null, o2: Pick<IProductionWaste, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductionWasteIdentifier(o1) === this.getProductionWasteIdentifier(o2) : o1 === o2;
  }

  addProductionWasteToCollectionIfMissing<Type extends Pick<IProductionWaste, 'id'>>(
    productionWasteCollection: Type[],
    ...productionWastesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productionWastes: Type[] = productionWastesToCheck.filter(isPresent);
    if (productionWastes.length > 0) {
      const productionWasteCollectionIdentifiers = productionWasteCollection.map(
        productionWasteItem => this.getProductionWasteIdentifier(productionWasteItem)!
      );
      const productionWastesToAdd = productionWastes.filter(productionWasteItem => {
        const productionWasteIdentifier = this.getProductionWasteIdentifier(productionWasteItem);
        if (productionWasteCollectionIdentifiers.includes(productionWasteIdentifier)) {
          return false;
        }
        productionWasteCollectionIdentifiers.push(productionWasteIdentifier);
        return true;
      });
      return [...productionWastesToAdd, ...productionWasteCollection];
    }
    return productionWasteCollection;
  }
}
