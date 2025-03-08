import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-add-transaction',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './add-transaction.component.html',
  styleUrl: './add-transaction.component.css'
})
export class AddTransactionComponent {
  transactionForm: FormGroup;
  customerId: string | null = '';

  constructor(private fb: FormBuilder, private route: ActivatedRoute){
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
      console.log("Transaction Data:", this.transactionForm.value);
      alert('Transaction added succesfully');
    }else{
      alert('Please fix errors in the form.')
    }
  }
}
