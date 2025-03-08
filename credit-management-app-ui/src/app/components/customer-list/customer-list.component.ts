import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer/customer.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-customer-list',
  imports: [CommonModule],
  templateUrl: './customer-list.component.html',
  styleUrl: './customer-list.component.css',
})
export class CustomerListComponent {
  customers: any[] = [];

  constructor(private customerService: CustomerService, private router: Router){}

  ngOnIt(): void {
    this.loadCustomers();
  }

  loadCustomers() {
    this.customerService.getCustomers().subscribe((data)=>{
      this.customers = data;
    });
  }

  viewTransactions(customerId: number): void {
    this.router.navigate(['/transactions', customerId]);
  }
}
