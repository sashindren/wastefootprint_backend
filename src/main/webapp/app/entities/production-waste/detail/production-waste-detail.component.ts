import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductionWaste } from '../production-waste.model';

@Component({
  selector: 'jhi-production-waste-detail',
  templateUrl: './production-waste-detail.component.html',
})
export class ProductionWasteDetailComponent implements OnInit {
  productionWaste: IProductionWaste | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productionWaste }) => {
      this.productionWaste = productionWaste;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
