import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmployeeDetails, NewEmployeeDetails } from '../employee-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeeDetails for edit and NewEmployeeDetailsFormGroupInput for create.
 */
type EmployeeDetailsFormGroupInput = IEmployeeDetails | PartialWithRequiredKeyOf<NewEmployeeDetails>;

type EmployeeDetailsFormDefaults = Pick<NewEmployeeDetails, 'id'>;

type EmployeeDetailsFormGroupContent = {
  id: FormControl<IEmployeeDetails['id'] | NewEmployeeDetails['id']>;
  employeeName: FormControl<IEmployeeDetails['employeeName']>;
  address: FormControl<IEmployeeDetails['address']>;
  phoneNumber: FormControl<IEmployeeDetails['phoneNumber']>;
  homeNumber: FormControl<IEmployeeDetails['homeNumber']>;
  emailAddress: FormControl<IEmployeeDetails['emailAddress']>;
  transportType: FormControl<IEmployeeDetails['transportType']>;
  jobTitle: FormControl<IEmployeeDetails['jobTitle']>;
  supervisorName: FormControl<IEmployeeDetails['supervisorName']>;
  companyId: FormControl<IEmployeeDetails['companyId']>;
  employeedetails: FormControl<IEmployeeDetails['employeedetails']>;
};

export type EmployeeDetailsFormGroup = FormGroup<EmployeeDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeDetailsFormService {
  createEmployeeDetailsFormGroup(employeeDetails: EmployeeDetailsFormGroupInput = { id: null }): EmployeeDetailsFormGroup {
    const employeeDetailsRawValue = {
      ...this.getFormDefaults(),
      ...employeeDetails,
    };
    return new FormGroup<EmployeeDetailsFormGroupContent>({
      id: new FormControl(
        { value: employeeDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      employeeName: new FormControl(employeeDetailsRawValue.employeeName),
      address: new FormControl(employeeDetailsRawValue.address),
      phoneNumber: new FormControl(employeeDetailsRawValue.phoneNumber),
      homeNumber: new FormControl(employeeDetailsRawValue.homeNumber),
      emailAddress: new FormControl(employeeDetailsRawValue.emailAddress),
      transportType: new FormControl(employeeDetailsRawValue.transportType),
      jobTitle: new FormControl(employeeDetailsRawValue.jobTitle),
      supervisorName: new FormControl(employeeDetailsRawValue.supervisorName),
      companyId: new FormControl(employeeDetailsRawValue.companyId),
      employeedetails: new FormControl(employeeDetailsRawValue.employeedetails),
    });
  }

  getEmployeeDetails(form: EmployeeDetailsFormGroup): IEmployeeDetails | NewEmployeeDetails {
    return form.getRawValue() as IEmployeeDetails | NewEmployeeDetails;
  }

  resetForm(form: EmployeeDetailsFormGroup, employeeDetails: EmployeeDetailsFormGroupInput): void {
    const employeeDetailsRawValue = { ...this.getFormDefaults(), ...employeeDetails };
    form.reset(
      {
        ...employeeDetailsRawValue,
        id: { value: employeeDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeDetailsFormDefaults {
    return {
      id: null,
    };
  }
}
