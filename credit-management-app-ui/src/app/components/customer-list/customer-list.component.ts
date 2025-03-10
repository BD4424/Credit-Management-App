import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../services/customer/customer.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-customer-list',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './customer-list.component.html',
  styleUrl: './customer-list.component.css',
})

export class CustomerListComponent implements OnInit{
  customers: any[] = [];

  constructor(private customerService: CustomerService, private router: Router, private http: HttpClient){}

  ngOnInit(): void {
    this.loadCustomers();
  }


  loadCustomers() {
    this.customerService.getCustomers().subscribe({
      next: (data) => {
        this.customers = data;
      },
      error: (err) => console.error('Error fetching customers:', err),
    });
  }

  viewTransactions(customerId: number): void {
    this.router.navigate(['/transactions', customerId]);
  }

  addTransactions(customerId: number): void {
    this.router.navigate(['/add-transaction',customerId])
  }
}
