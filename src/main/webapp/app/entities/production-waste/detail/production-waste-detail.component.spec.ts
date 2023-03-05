import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductionWasteDetailComponent } from './production-waste-detail.component';

describe('ProductionWaste Management Detail Component', () => {
  let comp: ProductionWasteDetailComponent;
  let fixture: ComponentFixture<ProductionWasteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductionWasteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productionWaste: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProductionWasteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductionWasteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productionWaste on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productionWaste).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
