import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TransactionService } from '../../services/transaction.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-transaction-list',
  imports: [CommonModule],
  templateUrl: './transaction-list.component.html',
  styleUrl: './transaction-list.component.css'
})
export class TransactionListComponent implements OnInit {
  transactions: any[] = [];
  customerId: number = 0;
  selectedFilters: Set<string> = new Set();
  filteredTransactions: any[] = [];

  constructor(private route: ActivatedRoute, private transactionService: TransactionService) { }

  ngOnInit(): void {
    this.customerId = Number(this.route.snapshot.paramMap.get('customerId'));
    this.loadTransactions();
  }

  loadTransactions() {
    this.transactionService.getTransactionsByCustomer(this.customerId).subscribe((data) => {
      this.transactions = data;
      this.filteredTransactions = [...this.transactions];
    })
  }

  markAsPaid(transactionId: number) {
    this.transactionService.updateTransactionAsPaid(transactionId).subscribe({
      next: (response) => {
        console.log('Transaction marked as paid', response);
        alert('Transaction marked as paid successfully');
        this.loadTransactions();
      },
      error: (err) => {
        console.error('Error updating transaction:', err);
      }
    });
  }

  sortTransactions(event: Event) {
    const selectedValue = (event.target as HTMLSelectElement).value;
    console.log("Sorting by:", selectedValue);

    if (selectedValue === "date-desc") {
      this.transactions.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
    } else if (selectedValue === "date-asc") {
      this.transactions.sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
    } else if (selectedValue === "amount-desc") {
      this.transactions.sort((a, b) => b.amount - a.amount);
    } else if (selectedValue === "amount-asc") {
      this.transactions.sort((a, b) => a.amount - b.amount);
    }
  }
  
  toggleFilter(status: string, event: Event) {
    const isChecked = (event.target as HTMLInputElement).checked;

    console.log(isChecked);

    if(isChecked){
      this.selectedFilters.add(status);
    } else {
      this.selectedFilters.delete(status);
    }
    this.applyFilters();
  }

  applyFilters() {
    console.log("Inside Apply Filter")
    if(this.selectedFilters.size === 0) {
      this.filteredTransactions = [...this.transactions];
    }else{
      this.filteredTransactions = this.transactions.filter(transaction => this.selectedFilters.has(transaction.status));
    }
  }

  
}
