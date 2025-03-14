import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { CustomerService } from '../../services/customer/customer.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-add-customer',
  imports: [CommonModule, ReactiveFormsModule, MatInputModule, MatFormFieldModule, MatButtonModule, MatSelectModule],
  templateUrl: './add-customer.component.html',
  styleUrl: './add-customer.component.css'
})
export class AddCustomerComponent {
  customerForm: FormGroup;

  constructor(private fb: FormBuilder, private customerService: CustomerService, private route: ActivatedRoute, private router: Router) {
    this.customerForm = this.fb.group({
      name: ['', Validators.required],
      phoneNo: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit() {
    if (this.customerForm.valid) {
      if (this.customerForm.valid) {
        const customerData = this.customerForm.value;
        this.customerService.addCustomer(customerData).subscribe({
          next: (response) => {
            console.log("Customer Data:", this.customerForm.value);
            console.log('Customer added successfully:', response);
            alert('Customer added successfully');
            console.log("Navigating to customers list page");
            this.router.navigate(['/customers']);
          }
        });
      } else {
        alert('Please fix errors in the form');
      }
    }
  }
}
