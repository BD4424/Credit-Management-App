import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionsDataComponent } from './transactions-data.component';

describe('TransactionsDataComponent', () => {
  let component: TransactionsDataComponent;
  let fixture: ComponentFixture<TransactionsDataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionsDataComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransactionsDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
