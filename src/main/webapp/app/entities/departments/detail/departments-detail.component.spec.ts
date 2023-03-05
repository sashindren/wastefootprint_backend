import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DepartmentsDetailComponent } from './departments-detail.component';

describe('Departments Management Detail Component', () => {
  let comp: DepartmentsDetailComponent;
  let fixture: ComponentFixture<DepartmentsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepartmentsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ departments: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DepartmentsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DepartmentsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load departments on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.departments).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
