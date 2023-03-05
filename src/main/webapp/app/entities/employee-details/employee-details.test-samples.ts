import { IEmployeeDetails, NewEmployeeDetails } from './employee-details.model';

export const sampleWithRequiredData: IEmployeeDetails = {
  id: 43079,
};

export const sampleWithPartialData: IEmployeeDetails = {
  id: 18767,
  homeNumber: 23765,
  emailAddress: 'JBOD Loan',
  transportType: 'wireless Equatorial',
  jobTitle: 'Lead Interactions Facilitator',
  companyId: 51573,
};

export const sampleWithFullData: IEmployeeDetails = {
  id: 65789,
  employeeName: 'project',
  address: 'Senior',
  phoneNumber: 89968,
  homeNumber: 57473,
  emailAddress: 'Forks Investment',
  transportType: 'York Passage',
  jobTitle: 'Lead Tactics Manager',
  supervisorName: 'Handcrafted generate PNG',
  companyId: 9118,
};

export const sampleWithNewData: NewEmployeeDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
