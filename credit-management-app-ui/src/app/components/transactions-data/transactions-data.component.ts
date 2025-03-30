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
import { toZonedTime } from 'date-fns-tz';

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
  ) { }

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
      params.fromDate = this.formatDateForBackend(this.filter.fromDate);
    }

    if (this.filter.toDate) {
      params.toDate = this.formatDateForBackend(this.filter.toDate);
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


  formatDateForBackend(date: Date): string {
    const localDate = new Date(date);
    localDate.setHours(12, 0, 0, 0); // Ensure it's in the middle of the day to prevent timezone shifts
    return localDate.toISOString().split('T')[0]; // Send only YYYY-MM-DD
  }

  exportToCSV() {
    // Get filtered data (respects pagination/sorting/filters)
    const dataToExport = this.dataSource.filteredData.length
      ? this.dataSource.filteredData
      : this.dataSource.data;

    if (dataToExport.length === 0) {
      alert('No transactions to export!');
      return;
    }

    const headers = ['Date', 'Item', 'Quantity', 'Amount (₹)', 'Status', 'Customer'];
    const csvRows = [];

    // Add headers
    csvRows.push(headers.join(','));

    // Add data rows
    dataToExport.forEach(transaction => {
      const row = [
        `"${format(new Date(transaction.date), 'yyyy-MM-dd HH:mm')}"`,
        `"${transaction.itemName.replace(/"/g, '""')}"`, // Escape quotes
        transaction.quantity,
        transaction.amount,
        transaction.status,
        `"${transaction.customerName.replace(/"/g, '""')}"`
      ];
      csvRows.push(row.join(','));
    });

    // Create and trigger download
    const csvContent = csvRows.join('\n');
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `transactions_${format(new Date(), 'yyyyMMdd_HHmmss')}.csv`;
    link.click();
    URL.revokeObjectURL(url);

  }

  exportToPdf() {
    const transactionIds = this.dataSource.filteredData.map(t => t.transactionId);
    const dateFormatter = new Intl.DateTimeFormat('en-IN', {
      timeZone: 'Asia/Kolkata',
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });

    this.transactionService.exportToPdf(transactionIds).subscribe({
      next: (pdfBlob: Blob) => {
        const url = window.URL.createObjectURL(pdfBlob);
        const a = document.createElement('a');
        a.href = url;
        const istTime = toZonedTime(new Date(), 'Asia/Kolkata');
        a.download = `transactions_${format(istTime, 'yyyyMMdd_HHmmss')}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('PDF export failed:', err);
      }
    })
  }
}