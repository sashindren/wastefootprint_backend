export interface IDepartments {
  id: number;
  departmentName?: string | null;
}

export type NewDepartments = Omit<IDepartments, 'id'> & { id: null };
