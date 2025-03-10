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
export class TransactionListComponent implements OnInit{
  transactions: any[] = [];
  customerId: number = 0;

  constructor( private route: ActivatedRoute, private transactionService: TransactionService){}

  ngOnInit(): void {
    this.customerId = Number(this.route.snapshot.paramMap.get('customerId'));
    this.loadTransactions();
  }

  loadTransactions() {
    this.transactionService.getTransactionsByCustomer(this.customerId).subscribe((data)=>{
      this.transactions = data;
    })
  }
}
