import { IDepartments } from 'app/entities/departments/departments.model';

export interface IProductionWaste {
  id: number;
  material?: string | null;
  quantity?: number | null;
  transportType?: string | null;
  electric?: number | null;
  water?: number | null;
  waste?: string | null;
  department?: Pick<IDepartments, 'id'> | null;
}

export type NewProductionWaste = Omit<IProductionWaste, 'id'> & { id: null };
