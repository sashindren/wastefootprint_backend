import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DepartmentsFormService } from './departments-form.service';
import { DepartmentsService } from '../service/departments.service';
import { IDepartments } from '../departments.model';

import { DepartmentsUpdateComponent } from './departments-update.component';

describe('Departments Management Update Component', () => {
  let comp: DepartmentsUpdateComponent;
  let fixture: ComponentFixture<DepartmentsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let departmentsFormService: DepartmentsFormService;
  let departmentsService: DepartmentsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DepartmentsUpdateComponent],
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
      .overrideTemplate(DepartmentsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepartmentsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    departmentsFormService = TestBed.inject(DepartmentsFormService);
    departmentsService = TestBed.inject(DepartmentsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const departments: IDepartments = { id: 456 };

      activatedRoute.data = of({ departments });
      comp.ngOnInit();

      expect(comp.departments).toEqual(departments);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartments>>();
      const departments = { id: 123 };
      jest.spyOn(departmentsFormService, 'getDepartments').mockReturnValue(departments);
      jest.spyOn(departmentsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departments });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departments }));
      saveSubject.complete();

      // THEN
      expect(departmentsFormService.getDepartments).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(departmentsService.update).toHaveBeenCalledWith(expect.objectContaining(departments));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartments>>();
      const departments = { id: 123 };
      jest.spyOn(departmentsFormService, 'getDepartments').mockReturnValue({ id: null });
      jest.spyOn(departmentsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departments: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departments }));
      saveSubject.complete();

      // THEN
      expect(departmentsFormService.getDepartments).toHaveBeenCalled();
      expect(departmentsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartments>>();
      const departments = { id: 123 };
      jest.spyOn(departmentsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departments });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(departmentsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
