import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductionWaste } from '../production-waste.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../production-waste.test-samples';

import { ProductionWasteService } from './production-waste.service';

const requireRestSample: IProductionWaste = {
  ...sampleWithRequiredData,
};

describe('ProductionWaste Service', () => {
  let service: ProductionWasteService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductionWaste | IProductionWaste[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductionWasteService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ProductionWaste', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const productionWaste = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productionWaste).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductionWaste', () => {
      const productionWaste = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productionWaste).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductionWaste', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductionWaste', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductionWaste', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductionWasteToCollectionIfMissing', () => {
      it('should add a ProductionWaste to an empty array', () => {
        const productionWaste: IProductionWaste = sampleWithRequiredData;
        expectedResult = service.addProductionWasteToCollectionIfMissing([], productionWaste);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productionWaste);
      });

      it('should not add a ProductionWaste to an array that contains it', () => {
        const productionWaste: IProductionWaste = sampleWithRequiredData;
        const productionWasteCollection: IProductionWaste[] = [
          {
            ...productionWaste,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductionWasteToCollectionIfMissing(productionWasteCollection, productionWaste);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductionWaste to an array that doesn't contain it", () => {
        const productionWaste: IProductionWaste = sampleWithRequiredData;
        const productionWasteCollection: IProductionWaste[] = [sampleWithPartialData];
        expectedResult = service.addProductionWasteToCollectionIfMissing(productionWasteCollection, productionWaste);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productionWaste);
      });

      it('should add only unique ProductionWaste to an array', () => {
        const productionWasteArray: IProductionWaste[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productionWasteCollection: IProductionWaste[] = [sampleWithRequiredData];
        expectedResult = service.addProductionWasteToCollectionIfMissing(productionWasteCollection, ...productionWasteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productionWaste: IProductionWaste = sampleWithRequiredData;
        const productionWaste2: IProductionWaste = sampleWithPartialData;
        expectedResult = service.addProductionWasteToCollectionIfMissing([], productionWaste, productionWaste2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productionWaste);
        expect(expectedResult).toContain(productionWaste2);
      });

      it('should accept null and undefined values', () => {
        const productionWaste: IProductionWaste = sampleWithRequiredData;
        expectedResult = service.addProductionWasteToCollectionIfMissing([], null, productionWaste, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productionWaste);
      });

      it('should return initial array if no ProductionWaste is added', () => {
        const productionWasteCollection: IProductionWaste[] = [sampleWithRequiredData];
        expectedResult = service.addProductionWasteToCollectionIfMissing(productionWasteCollection, undefined, null);
        expect(expectedResult).toEqual(productionWasteCollection);
      });
    });

    describe('compareProductionWaste', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductionWaste(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProductionWaste(entity1, entity2);
        const compareResult2 = service.compareProductionWaste(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProductionWaste(entity1, entity2);
        const compareResult2 = service.compareProductionWaste(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProductionWaste(entity1, entity2);
        const compareResult2 = service.compareProductionWaste(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
