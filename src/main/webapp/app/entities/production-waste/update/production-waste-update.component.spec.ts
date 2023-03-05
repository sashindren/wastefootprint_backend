import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductionWasteFormService } from './production-waste-form.service';
import { ProductionWasteService } from '../service/production-waste.service';
import { IProductionWaste } from '../production-waste.model';
import { IDepartments } from 'app/entities/departments/departments.model';
import { DepartmentsService } from 'app/entities/departments/service/departments.service';

import { ProductionWasteUpdateComponent } from './production-waste-update.component';

describe('ProductionWaste Management Update Component', () => {
  let comp: ProductionWasteUpdateComponent;
  let fixture: ComponentFixture<ProductionWasteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productionWasteFormService: ProductionWasteFormService;
  let productionWasteService: ProductionWasteService;
  let departmentsService: DepartmentsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductionWasteUpdateComponent],
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
      .overrideTemplate(ProductionWasteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductionWasteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productionWasteFormService = TestBed.inject(ProductionWasteFormService);
    productionWasteService = TestBed.inject(ProductionWasteService);
    departmentsService = TestBed.inject(DepartmentsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Departments query and add missing value', () => {
      const productionWaste: IProductionWaste = { id: 456 };
      const department: IDepartments = { id: 72283 };
      productionWaste.department = department;

      const departmentsCollection: IDepartments[] = [{ id: 42337 }];
      jest.spyOn(departmentsService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentsCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartments[] = [...additionalDepartments, ...departmentsCollection];
      jest.spyOn(departmentsService, 'addDepartmentsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productionWaste });
      comp.ngOnInit();

      expect(departmentsService.query).toHaveBeenCalled();
      expect(departmentsService.addDepartmentsToCollectionIfMissing).toHaveBeenCalledWith(
        departmentsCollection,
        ...additionalDepartments.map(expect.objectContaining)
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productionWaste: IProductionWaste = { id: 456 };
      const department: IDepartments = { id: 77451 };
      productionWaste.department = department;

      activatedRoute.data = of({ productionWaste });
      comp.ngOnInit();

      expect(comp.departmentsSharedCollection).toContain(department);
      expect(comp.productionWaste).toEqual(productionWaste);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductionWaste>>();
      const productionWaste = { id: 123 };
      jest.spyOn(productionWasteFormService, 'getProductionWaste').mockReturnValue(productionWaste);
      jest.spyOn(productionWasteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productionWaste });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productionWaste }));
      saveSubject.complete();

      // THEN
      expect(productionWasteFormService.getProductionWaste).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productionWasteService.update).toHaveBeenCalledWith(expect.objectContaining(productionWaste));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductionWaste>>();
      const productionWaste = { id: 123 };
      jest.spyOn(productionWasteFormService, 'getProductionWaste').mockReturnValue({ id: null });
      jest.spyOn(productionWasteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productionWaste: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productionWaste }));
      saveSubject.complete();

      // THEN
      expect(productionWasteFormService.getProductionWaste).toHaveBeenCalled();
      expect(productionWasteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductionWaste>>();
      const productionWaste = { id: 123 };
      jest.spyOn(productionWasteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productionWaste });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productionWasteService.update).toHaveBeenCalled();
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
