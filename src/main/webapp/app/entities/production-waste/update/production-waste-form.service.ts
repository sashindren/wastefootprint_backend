import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductionWaste, NewProductionWaste } from '../production-waste.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductionWaste for edit and NewProductionWasteFormGroupInput for create.
 */
type ProductionWasteFormGroupInput = IProductionWaste | PartialWithRequiredKeyOf<NewProductionWaste>;

type ProductionWasteFormDefaults = Pick<NewProductionWaste, 'id'>;

type ProductionWasteFormGroupContent = {
  id: FormControl<IProductionWaste['id'] | NewProductionWaste['id']>;
  material: FormControl<IProductionWaste['material']>;
  quantity: FormControl<IProductionWaste['quantity']>;
  transportType: FormControl<IProductionWaste['transportType']>;
  electric: FormControl<IProductionWaste['electric']>;
  water: FormControl<IProductionWaste['water']>;
  waste: FormControl<IProductionWaste['waste']>;
  department: FormControl<IProductionWaste['department']>;
};

export type ProductionWasteFormGroup = FormGroup<ProductionWasteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductionWasteFormService {
  createProductionWasteFormGroup(productionWaste: ProductionWasteFormGroupInput = { id: null }): ProductionWasteFormGroup {
    const productionWasteRawValue = {
      ...this.getFormDefaults(),
      ...productionWaste,
    };
    return new FormGroup<ProductionWasteFormGroupContent>({
      id: new FormControl(
        { value: productionWasteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      material: new FormControl(productionWasteRawValue.material),
      quantity: new FormControl(productionWasteRawValue.quantity),
      transportType: new FormControl(productionWasteRawValue.transportType),
      electric: new FormControl(productionWasteRawValue.electric),
      water: new FormControl(productionWasteRawValue.water),
      waste: new FormControl(productionWasteRawValue.waste),
      department: new FormControl(productionWasteRawValue.department),
    });
  }

  getProductionWaste(form: ProductionWasteFormGroup): IProductionWaste | NewProductionWaste {
    return form.getRawValue() as IProductionWaste | NewProductionWaste;
  }

  resetForm(form: ProductionWasteFormGroup, productionWaste: ProductionWasteFormGroupInput): void {
    const productionWasteRawValue = { ...this.getFormDefaults(), ...productionWaste };
    form.reset(
      {
        ...productionWasteRawValue,
        id: { value: productionWasteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductionWasteFormDefaults {
    return {
      id: null,
    };
  }
}
