import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { TransactionService } from '../../services/transaction.service';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { format } from 'date-fns';

@Component({
  selector: 'app-transactions-data',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatSelectModule,
    FormsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule, 
    MatTableModule 
  ],
  templateUrl: './transactions-data.component.html',
  styleUrl: './transactions-data.component.css'
})
export class TransactionsDataComponent {
  transactions: any[] = [];
  displayedColumns: string[] = ['date', 'itemName', 'quantity', 'amount', 'status', 'customer', 'action'];
  filter = {
    fromDate: undefined as Date | undefined,
    toDate: undefined as Date | undefined,
    status: 'ALL'
  };

  constructor(private transactionService: TransactionService) { }

  ngOnInit(): void {
    this.loadAllTransactions();
  }

  loadAllTransactions() {
    this.transactionService.allTransactions().subscribe((data) => {
      this.transactions = data;
      console.log("All transactions", this.transactions);
    });
  }

  markAsPaid(transactionId: number) {
    this.transactionService.updateTransactionAsPaid(transactionId).subscribe({
      next: (response) => {
        console.log('Transaction marked as paid', response);
        alert('Transaction marked as paid successfully');
        this.loadAllTransactions();
      },
      error: (err) => console.error('Error updating transaction:', err)
    });
  }

  applyFilters() {
    console.log('Filters Applied:', this.filter);
    this.fetchFilteredTransactions();
  }

  fetchFilteredTransactions() {
    // Convert dates to ISO string (YYYY-MM-DD) or send null if not defined
    const params: any = {
      status: this.filter.status !== 'ALL' ? this.filter.status : null
    };
  
    if (this.filter.fromDate) {
      console.log(this.filter.fromDate);
      params.fromDate = this.convertToDateString(this.filter.fromDate);
      console.log(params.fromDate);
    }
  
    if (this.filter.toDate) {
      params.toDate = this.convertToDateString(this.filter.toDate);
      console.log(params.toDate);
    }
  
    console.log('Request params:', params);
  
    this.transactionService.getTransactions(params.fromDate, params.toDate, params.status)
      .subscribe({
        next: (response) => {
          this.transactions = response;
          this.updateCharts();
        },
        error: (err) => console.error('Error fetching transactions:', err)
      });
  }

  updateCharts() {
    console.log("Updating charts...");
    // TODO: Implement chart update logic
  }

  convertToDateString(date: Date): string {
    return format(date, 'yyyy-MM-dd'); // Converts Date to 'YYYY-MM-DD'
  }
}
