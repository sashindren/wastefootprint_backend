import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee-details.test-samples';

import { EmployeeDetailsFormService } from './employee-details-form.service';

describe('EmployeeDetails Form Service', () => {
  let service: EmployeeDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeName: expect.any(Object),
            address: expect.any(Object),
            phoneNumber: expect.any(Object),
            homeNumber: expect.any(Object),
            emailAddress: expect.any(Object),
            transportType: expect.any(Object),
            jobTitle: expect.any(Object),
            supervisorName: expect.any(Object),
            companyId: expect.any(Object),
            employeedetails: expect.any(Object),
          })
        );
      });

      it('passing IEmployeeDetails should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            employeeName: expect.any(Object),
            address: expect.any(Object),
            phoneNumber: expect.any(Object),
            homeNumber: expect.any(Object),
            emailAddress: expect.any(Object),
            transportType: expect.any(Object),
            jobTitle: expect.any(Object),
            supervisorName: expect.any(Object),
            companyId: expect.any(Object),
            employeedetails: expect.any(Object),
          })
        );
      });
    });

    describe('getEmployeeDetails', () => {
      it('should return NewEmployeeDetails for default EmployeeDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmployeeDetailsFormGroup(sampleWithNewData);

        const employeeDetails = service.getEmployeeDetails(formGroup) as any;

        expect(employeeDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployeeDetails for empty EmployeeDetails initial value', () => {
        const formGroup = service.createEmployeeDetailsFormGroup();

        const employeeDetails = service.getEmployeeDetails(formGroup) as any;

        expect(employeeDetails).toMatchObject({});
      });

      it('should return IEmployeeDetails', () => {
        const formGroup = service.createEmployeeDetailsFormGroup(sampleWithRequiredData);

        const employeeDetails = service.getEmployeeDetails(formGroup) as any;

        expect(employeeDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployeeDetails should not enable id FormControl', () => {
        const formGroup = service.createEmployeeDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployeeDetails should disable id FormControl', () => {
        const formGroup = service.createEmployeeDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
