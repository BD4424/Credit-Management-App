import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { TransactionService } from '../../services/transaction.service';

@Component({
  selector: 'app-transactions-data',
  imports: [CommonModule, MatCardModule],
  templateUrl: './transactions-data.component.html',
  styleUrl: './transactions-data.component.css'
})
export class TransactionsDataComponent {

  transactions: any[] = [];
  constructor(private transactionService: TransactionService) { }

  ngOnInit(): void {
    this.loadAllTransactions();
  }

  loadAllTransactions() {
    this.transactionService.allTransactions().subscribe((data)=>{
      this.transactions = data;
      console.log("All transactions");
    });
  }

  markAsPaid(transactionId: number) {
    this.transactionService.updateTransactionAsPaid(transactionId).subscribe({
      next: (response) => {
        console.log('Transaction marked as paid', response);
        alert('Transaction marked as paid successfully');
        this.loadAllTransactions();
      },
      error: (err) => {
        console.error('Error updating transaction:', err);
      }
    });
  }

}
