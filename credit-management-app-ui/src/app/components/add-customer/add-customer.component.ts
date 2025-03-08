import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-add-customer',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './add-customer.component.html',
  styleUrl: './add-customer.component.css'
})
export class AddCustomerComponent {
  customerForm: FormGroup;

  constructor(private fb: FormBuilder){
    this.customerForm = this.fb.group({
      name: ['', Validators.required],
      phoneNo: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit() {
    if(this.customerForm.valid){
      console.log("Customer Data:",this.customerForm.value);
      alert('Customer added successfully');
    }else{
      alert('Please fix errors in the form');
    }
  }
}
