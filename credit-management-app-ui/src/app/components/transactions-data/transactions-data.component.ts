import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectorRef, Component, CUSTOM_ELEMENTS_SCHEMA, ViewChild } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { TransactionService } from '../../services/transaction.service';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatButtonModule } from '@angular/material/button';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { format } from 'date-fns';

interface Transaction {
  date: Date;
  itemName: string;
  quantity: number;
  amount: number;
  status: 'PAID' | 'PENDING';
  customerName: string;
  transactionId: number;
}

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
    MatTableModule,
    MatPaginatorModule,
    MatSortModule
  ],
  templateUrl: './transactions-data.component.html',
  styleUrls: ['./transactions-data.component.css'],
  schemas: [CUSTOM_ELEMENTS_SCHEMA] 
})

export class TransactionsDataComponent implements AfterViewInit {
  displayedColumns: string[] = ['date', 'itemName', 'quantity', 'amount', 'status', 'customer', 'action'];
  dataSource = new MatTableDataSource<Transaction>([]);
  
  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;
  @ViewChild(MatSort, { static: false }) sort!: MatSort;

  filter = {
    fromDate: undefined as Date | undefined,
    toDate: undefined as Date | undefined,
    status: 'ALL'
  };

  isLoading = false;

  constructor(
    private transactionService: TransactionService,
    private cdr: ChangeDetectorRef
  ) {}

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
  

  ngOnInit() {
    this.loadAllTransactions();
  }

  loadAllTransactions() {
    this.isLoading = true;
    this.transactionService.allTransactions().subscribe({
      next: (data = []) => {
        this.dataSource = new MatTableDataSource(data); // Reinitialize dataSource
        this.isLoading = false;
  
        setTimeout(() => {
          this.dataSource.paginator = this.paginator; // Assign paginator
          this.dataSource.sort = this.sort; // Assign sorting
          this.cdr.detectChanges();
        });
      },
      error: (err) => {
        console.error('Error loading transactions:', err);
        this.dataSource.data = [];
        this.isLoading = false;
      }
    });
  }

  markAsPaid(transactionId: number) {
    this.transactionService.updateTransactionAsPaid(transactionId).subscribe({
      next: () => this.loadAllTransactions(),
      error: (err) => console.error('Error updating transaction:', err)
    });
  }

  applyFilters() {
    const params: any = {
      status: this.filter.status !== 'ALL' ? this.filter.status : null
    };
  
    if (this.filter.fromDate) {
      params.fromDate = this.formatDate(this.filter.fromDate);
    }
  
    if (this.filter.toDate) {
      params.toDate = this.formatDate(this.filter.toDate);
    }
  
    this.transactionService.getTransactions(params.fromDate, params.toDate, params.status)
      .subscribe({
        next: (response) => {
          this.dataSource = new MatTableDataSource(response); // Reinitialize dataSource
          setTimeout(() => {
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
            this.paginator.firstPage(); // Reset to first page
          });
        },
        error: (err) => console.error('Error fetching transactions:', err)
      });
  }
  

  private formatDate(date: Date): string {
    return date.toISOString().split('T')[0];
  }
}