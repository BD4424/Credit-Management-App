import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TransactionService } from '../../services/transaction.service';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';  // ✅ Add this line

@Component({
  selector: 'app-add-transaction',
  standalone: true,  // ✅ If using standalone components
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatSelectModule,
    MatOptionModule  // ✅ Add this here
  ],
  templateUrl: './add-transaction.component.html',
  styleUrls: ['./add-transaction.component.css']
})
export class AddTransactionComponent {
  transactionForm: FormGroup;
  customerId: string | null = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private transactionService: TransactionService,
    private router: Router
  ) {
    this.customerId = this.route.snapshot.paramMap.get('customerId');

    this.transactionForm = this.fb.group({
      transactions: this.fb.array([this.createTransaction()])
    });
  }

  createTransaction(): FormGroup {
    return this.fb.group({
      itemName: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1)]],
      amount: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
      status: ['PENDING', Validators.required],
      customerId: [this.customerId]
    });
  }

  get transactions(): FormArray {
    return this.transactionForm.get('transactions') as FormArray;
  }
  
  addTransaction(): void {
    this.transactions.push(this.createTransaction());
  }
  
  removeTransaction(index: number): void {
    this.transactions.removeAt(index);
  }

  onSubmit(): void {
    if (this.transactionForm.valid) {
      const transactionData = this.transactionForm.value.transactions;
      console.log("Transaction Data:", this.transactionForm.value);
      
      this.transactionService.addTransactionForCustomer(transactionData).subscribe({
        next: (response) => {
          console.log('Transaction added successfully:', response);
          alert('Transaction added successfully');
          this.router.navigate(['/transactions', this.customerId]);
        },
        error: (err) => {
          console.error('Error adding transaction:', err);
          alert('Failed to add transaction. Please try again.');
        }
      });
    } else {
      alert('Please fix errors in the form.');
    }
  }
}
