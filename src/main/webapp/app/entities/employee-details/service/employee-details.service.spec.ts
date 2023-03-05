import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmployeeDetails } from '../employee-details.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../employee-details.test-samples';

import { EmployeeDetailsService } from './employee-details.service';

const requireRestSample: IEmployeeDetails = {
  ...sampleWithRequiredData,
};

describe('EmployeeDetails Service', () => {
  let service: EmployeeDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmployeeDetails | IEmployeeDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmployeeDetailsService);
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

    it('should create a EmployeeDetails', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const employeeDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(employeeDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmployeeDetails', () => {
      const employeeDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(employeeDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmployeeDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmployeeDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmployeeDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmployeeDetailsToCollectionIfMissing', () => {
      it('should add a EmployeeDetails to an empty array', () => {
        const employeeDetails: IEmployeeDetails = sampleWithRequiredData;
        expectedResult = service.addEmployeeDetailsToCollectionIfMissing([], employeeDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeDetails);
      });

      it('should not add a EmployeeDetails to an array that contains it', () => {
        const employeeDetails: IEmployeeDetails = sampleWithRequiredData;
        const employeeDetailsCollection: IEmployeeDetails[] = [
          {
            ...employeeDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmployeeDetailsToCollectionIfMissing(employeeDetailsCollection, employeeDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmployeeDetails to an array that doesn't contain it", () => {
        const employeeDetails: IEmployeeDetails = sampleWithRequiredData;
        const employeeDetailsCollection: IEmployeeDetails[] = [sampleWithPartialData];
        expectedResult = service.addEmployeeDetailsToCollectionIfMissing(employeeDetailsCollection, employeeDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeDetails);
      });

      it('should add only unique EmployeeDetails to an array', () => {
        const employeeDetailsArray: IEmployeeDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const employeeDetailsCollection: IEmployeeDetails[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeDetailsToCollectionIfMissing(employeeDetailsCollection, ...employeeDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const employeeDetails: IEmployeeDetails = sampleWithRequiredData;
        const employeeDetails2: IEmployeeDetails = sampleWithPartialData;
        expectedResult = service.addEmployeeDetailsToCollectionIfMissing([], employeeDetails, employeeDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(employeeDetails);
        expect(expectedResult).toContain(employeeDetails2);
      });

      it('should accept null and undefined values', () => {
        const employeeDetails: IEmployeeDetails = sampleWithRequiredData;
        expectedResult = service.addEmployeeDetailsToCollectionIfMissing([], null, employeeDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(employeeDetails);
      });

      it('should return initial array if no EmployeeDetails is added', () => {
        const employeeDetailsCollection: IEmployeeDetails[] = [sampleWithRequiredData];
        expectedResult = service.addEmployeeDetailsToCollectionIfMissing(employeeDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(employeeDetailsCollection);
      });
    });

    describe('compareEmployeeDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmployeeDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmployeeDetails(entity1, entity2);
        const compareResult2 = service.compareEmployeeDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmployeeDetails(entity1, entity2);
        const compareResult2 = service.compareEmployeeDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmployeeDetails(entity1, entity2);
        const compareResult2 = service.compareEmployeeDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
