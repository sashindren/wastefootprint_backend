import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeeDetails, NewEmployeeDetails } from '../employee-details.model';

export type PartialUpdateEmployeeDetails = Partial<IEmployeeDetails> & Pick<IEmployeeDetails, 'id'>;

export type EntityResponseType = HttpResponse<IEmployeeDetails>;
export type EntityArrayResponseType = HttpResponse<IEmployeeDetails[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeeDetails: NewEmployeeDetails): Observable<EntityResponseType> {
    return this.http.post<IEmployeeDetails>(this.resourceUrl, employeeDetails, { observe: 'response' });
  }

  update(employeeDetails: IEmployeeDetails): Observable<EntityResponseType> {
    return this.http.put<IEmployeeDetails>(`${this.resourceUrl}/${this.getEmployeeDetailsIdentifier(employeeDetails)}`, employeeDetails, {
      observe: 'response',
    });
  }

  partialUpdate(employeeDetails: PartialUpdateEmployeeDetails): Observable<EntityResponseType> {
    return this.http.patch<IEmployeeDetails>(`${this.resourceUrl}/${this.getEmployeeDetailsIdentifier(employeeDetails)}`, employeeDetails, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmployeeDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmployeeDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeeDetailsIdentifier(employeeDetails: Pick<IEmployeeDetails, 'id'>): number {
    return employeeDetails.id;
  }

  compareEmployeeDetails(o1: Pick<IEmployeeDetails, 'id'> | null, o2: Pick<IEmployeeDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeeDetailsIdentifier(o1) === this.getEmployeeDetailsIdentifier(o2) : o1 === o2;
  }

  addEmployeeDetailsToCollectionIfMissing<Type extends Pick<IEmployeeDetails, 'id'>>(
    employeeDetailsCollection: Type[],
    ...employeeDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeeDetails: Type[] = employeeDetailsToCheck.filter(isPresent);
    if (employeeDetails.length > 0) {
      const employeeDetailsCollectionIdentifiers = employeeDetailsCollection.map(
        employeeDetailsItem => this.getEmployeeDetailsIdentifier(employeeDetailsItem)!
      );
      const employeeDetailsToAdd = employeeDetails.filter(employeeDetailsItem => {
        const employeeDetailsIdentifier = this.getEmployeeDetailsIdentifier(employeeDetailsItem);
        if (employeeDetailsCollectionIdentifiers.includes(employeeDetailsIdentifier)) {
          return false;
        }
        employeeDetailsCollectionIdentifiers.push(employeeDetailsIdentifier);
        return true;
      });
      return [...employeeDetailsToAdd, ...employeeDetailsCollection];
    }
    return employeeDetailsCollection;
  }
}
