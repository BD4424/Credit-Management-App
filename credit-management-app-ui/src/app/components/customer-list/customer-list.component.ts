import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../services/customer/customer.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RemindersService } from '../../services/reminders.service';

@Component({
  selector: 'app-customer-list',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './customer-list.component.html',
  styleUrl: './customer-list.component.css',
})

export class CustomerListComponent implements OnInit{
  customers: any[] = [];

  constructor(private customerService: CustomerService, private router: Router, private http: HttpClient, private reminderService: RemindersService){}

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

  sendReminder(customerId: number): void {
    this.reminderService.sendReminderEmailToCustomer(customerId).subscribe({
      next: (response) => {
        console.log("Reminder sent successfully:", response);
        alert('Reminder sent successfully!');
      },
      error: (err)=> {
        console.error('Error sending reminder:', err);
        alert('Failed to send reminder. Try again.')
      }
    });
  }
}
