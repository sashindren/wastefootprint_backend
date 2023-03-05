import { IProductionWaste, NewProductionWaste } from './production-waste.model';

export const sampleWithRequiredData: IProductionWaste = {
  id: 51187,
};

export const sampleWithPartialData: IProductionWaste = {
  id: 95202,
  material: 'Rubber Liaison copying',
  quantity: 72298,
  electric: 80894,
  waste: 'payment',
};

export const sampleWithFullData: IProductionWaste = {
  id: 67354,
  material: 'Lithuania',
  quantity: 9608,
  transportType: 'Designer e-business',
  electric: 92626,
  water: 9469,
  waste: 'interface',
};

export const sampleWithNewData: NewProductionWaste = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
