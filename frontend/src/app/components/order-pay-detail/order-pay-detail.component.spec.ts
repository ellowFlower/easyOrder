import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderPayDetailComponent } from './order-pay-detail.component';

describe('OrderPayDetailComponent', () => {
  let component: OrderPayDetailComponent;
  let fixture: ComponentFixture<OrderPayDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OrderPayDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderPayDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
