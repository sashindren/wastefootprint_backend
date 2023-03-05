import { IDepartments } from 'app/entities/departments/departments.model';

export interface IEmployeeDetails {
  id: number;
  employeeName?: string | null;
  address?: string | null;
  phoneNumber?: number | null;
  homeNumber?: number | null;
  emailAddress?: string | null;
  transportType?: string | null;
  jobTitle?: string | null;
  supervisorName?: string | null;
  companyId?: number | null;
  employeedetails?: Pick<IDepartments, 'id'> | null;
}

export type NewEmployeeDetails = Omit<IEmployeeDetails, 'id'> & { id: null };
