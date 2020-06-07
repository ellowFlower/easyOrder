import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistertableComponent } from './registertable.component';

describe('RegistertableComponent', () => {
  let component: RegistertableComponent;
  let fixture: ComponentFixture<RegistertableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegistertableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistertableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
