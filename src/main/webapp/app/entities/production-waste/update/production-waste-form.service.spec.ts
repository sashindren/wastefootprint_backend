import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../production-waste.test-samples';

import { ProductionWasteFormService } from './production-waste-form.service';

describe('ProductionWaste Form Service', () => {
  let service: ProductionWasteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductionWasteFormService);
  });

  describe('Service methods', () => {
    describe('createProductionWasteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductionWasteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            material: expect.any(Object),
            quantity: expect.any(Object),
            transportType: expect.any(Object),
            electric: expect.any(Object),
            water: expect.any(Object),
            waste: expect.any(Object),
            department: expect.any(Object),
          })
        );
      });

      it('passing IProductionWaste should create a new form with FormGroup', () => {
        const formGroup = service.createProductionWasteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            material: expect.any(Object),
            quantity: expect.any(Object),
            transportType: expect.any(Object),
            electric: expect.any(Object),
            water: expect.any(Object),
            waste: expect.any(Object),
            department: expect.any(Object),
          })
        );
      });
    });

    describe('getProductionWaste', () => {
      it('should return NewProductionWaste for default ProductionWaste initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProductionWasteFormGroup(sampleWithNewData);

        const productionWaste = service.getProductionWaste(formGroup) as any;

        expect(productionWaste).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductionWaste for empty ProductionWaste initial value', () => {
        const formGroup = service.createProductionWasteFormGroup();

        const productionWaste = service.getProductionWaste(formGroup) as any;

        expect(productionWaste).toMatchObject({});
      });

      it('should return IProductionWaste', () => {
        const formGroup = service.createProductionWasteFormGroup(sampleWithRequiredData);

        const productionWaste = service.getProductionWaste(formGroup) as any;

        expect(productionWaste).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductionWaste should not enable id FormControl', () => {
        const formGroup = service.createProductionWasteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductionWaste should disable id FormControl', () => {
        const formGroup = service.createProductionWasteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
