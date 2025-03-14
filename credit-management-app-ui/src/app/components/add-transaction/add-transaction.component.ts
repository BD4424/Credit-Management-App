import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TransactionService } from '../../services/transaction.service';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-add-transaction',
  imports: [CommonModule, ReactiveFormsModule, MatInputModule, MatFormFieldModule, MatButtonModule, MatSelectModule],
  templateUrl: './add-transaction.component.html',
  styleUrl: './add-transaction.component.css'
})
export class AddTransactionComponent {
  transactionForm: FormGroup;
  customerId: string | null = '';

  constructor(private fb: FormBuilder, private route: ActivatedRoute, private transactionService: TransactionService, private router: Router){
    this.customerId = this.route.snapshot.paramMap.get('customerId');

    this.transactionForm = this.fb.group({
      itemName: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1)]],
      amount: ['',[Validators.required, Validators.pattern('^[0-9]+$')]] ,
      status: ['PENDING', Validators.required],
      customerId: [this.customerId]
    });
  }

  onSubmit(){
    if(this.transactionForm.valid){
      const transactionData = this.transactionForm.value;
      console.log("Transaction Data:", this.transactionForm.value);
      
      this.transactionService.addTransactionForCustomer(transactionData).subscribe({
        next: (response) => {
          console.log('Transaction added successfully:', response);
          alert('Transaction added succesfully');
          console.log("Navigating to transactions page with ID:", this.customerId);
          this.router.navigate(['/transactions', this.customerId]);
        }
      })
      
    }else{
      alert('Please fix errors in the form.')
    }
  }
}
