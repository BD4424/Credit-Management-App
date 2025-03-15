import { provideRouter, RouterModule, Routes } from '@angular/router';
import { CustomerListComponent } from './components/customer-list/customer-list.component';
import { importProvidersFrom, NgModule } from '@angular/core';
import { AddCustomerComponent } from './components/add-customer/add-customer.component';
import { TransactionListComponent } from './components/transaction-list/transaction-list.component';
import { AddTransactionComponent } from './components/add-transaction/add-transaction.component';
import { RemindersComponent } from './components/reminders/reminders.component';
import { ReactiveFormsModule } from '@angular/forms';

// Import Angular Material Modules
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { provideHttpClient } from '@angular/common/http';
import { TransactionsDataComponent } from './components/transactions-data/transactions-data.component';

export const routes: Routes = [
    { path: '', redirectTo: 'customers', pathMatch: 'full' },
    { path: 'customers', component: CustomerListComponent },
    { path: 'add-customer', component: AddCustomerComponent },
    { path: 'transactions/:customerId', component: TransactionListComponent },
    { path: 'add-transaction/:customerId', component: AddTransactionComponent },
    { path: 'reminders', component: RemindersComponent },
    { path: 'transactions', component: TransactionsDataComponent }
];


export const routing = RouterModule.forRoot(routes);

bootstrapApplication(AppComponent, {
    providers: [
      provideRouter(routes),
      provideHttpClient(),
      importProvidersFrom(
        MatInputModule,
        MatFormFieldModule,
        MatButtonModule,
        MatSelectModule
      )
    ]
  });