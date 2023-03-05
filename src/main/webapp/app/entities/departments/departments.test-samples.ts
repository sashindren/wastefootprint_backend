import { IDepartments, NewDepartments } from './departments.model';

export const sampleWithRequiredData: IDepartments = {
  id: 19054,
};

export const sampleWithPartialData: IDepartments = {
  id: 19256,
  departmentName: 'pink intuitive',
};

export const sampleWithFullData: IDepartments = {
  id: 45636,
  departmentName: 'Concrete',
};

export const sampleWithNewData: NewDepartments = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
