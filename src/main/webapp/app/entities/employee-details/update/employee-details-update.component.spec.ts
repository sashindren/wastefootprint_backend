import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeDetailsFormService } from './employee-details-form.service';
import { EmployeeDetailsService } from '../service/employee-details.service';
import { IEmployeeDetails } from '../employee-details.model';
import { IDepartments } from 'app/entities/departments/departments.model';
import { DepartmentsService } from 'app/entities/departments/service/departments.service';

import { EmployeeDetailsUpdateComponent } from './employee-details-update.component';

describe('EmployeeDetails Management Update Component', () => {
  let comp: EmployeeDetailsUpdateComponent;
  let fixture: ComponentFixture<EmployeeDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeDetailsFormService: EmployeeDetailsFormService;
  let employeeDetailsService: EmployeeDetailsService;
  let departmentsService: DepartmentsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeDetailsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EmployeeDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeDetailsFormService = TestBed.inject(EmployeeDetailsFormService);
    employeeDetailsService = TestBed.inject(EmployeeDetailsService);
    departmentsService = TestBed.inject(DepartmentsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Departments query and add missing value', () => {
      const employeeDetails: IEmployeeDetails = { id: 456 };
      const employeedetails: IDepartments = { id: 30784 };
      employeeDetails.employeedetails = employeedetails;

      const departmentsCollection: IDepartments[] = [{ id: 36028 }];
      jest.spyOn(departmentsService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentsCollection })));
      const additionalDepartments = [employeedetails];
      const expectedCollection: IDepartments[] = [...additionalDepartments, ...departmentsCollection];
      jest.spyOn(departmentsService, 'addDepartmentsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employeeDetails });
      comp.ngOnInit();

      expect(departmentsService.query).toHaveBeenCalled();
      expect(departmentsService.addDepartmentsToCollectionIfMissing).toHaveBeenCalledWith(
        departmentsCollection,
        ...additionalDepartments.map(expect.objectContaining)
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employeeDetails: IEmployeeDetails = { id: 456 };
      const employeedetails: IDepartments = { id: 26236 };
      employeeDetails.employeedetails = employeedetails;

      activatedRoute.data = of({ employeeDetails });
      comp.ngOnInit();

      expect(comp.departmentsSharedCollection).toContain(employeedetails);
      expect(comp.employeeDetails).toEqual(employeeDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeDetails>>();
      const employeeDetails = { id: 123 };
      jest.spyOn(employeeDetailsFormService, 'getEmployeeDetails').mockReturnValue(employeeDetails);
      jest.spyOn(employeeDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeDetails }));
      saveSubject.complete();

      // THEN
      expect(employeeDetailsFormService.getEmployeeDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(employeeDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeDetails>>();
      const employeeDetails = { id: 123 };
      jest.spyOn(employeeDetailsFormService, 'getEmployeeDetails').mockReturnValue({ id: null });
      jest.spyOn(employeeDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employeeDetails }));
      saveSubject.complete();

      // THEN
      expect(employeeDetailsFormService.getEmployeeDetails).toHaveBeenCalled();
      expect(employeeDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployeeDetails>>();
      const employeeDetails = { id: 123 };
      jest.spyOn(employeeDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employeeDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDepartments', () => {
      it('Should forward to departmentsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departmentsService, 'compareDepartments');
        comp.compareDepartments(entity, entity2);
        expect(departmentsService.compareDepartments).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
