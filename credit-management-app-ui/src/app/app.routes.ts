import { RouterModule, Routes } from '@angular/router';
import { CustomerListComponent } from './components/customer-list/customer-list.component';
import { NgModule } from '@angular/core';
import { AddCustomerComponent } from './components/add-customer/add-customer.component';
import { TransactionListComponent } from './components/transaction-list/transaction-list.component';
import { AddTransactionComponent } from './components/add-transaction/add-transaction.component';
import { RemindersComponent } from './components/reminders/reminders.component';
import { ReactiveFormsModule } from '@angular/forms';

export const routes: Routes = [
    { path: '', redirectTo: 'customers', pathMatch: 'full' },
    { path: 'customers', component: CustomerListComponent },
    { path: 'add-customer', component: AddCustomerComponent },
    { path: 'transactions/:customerId', component: TransactionListComponent },
    { path: 'add-transaction/:customerId', component: AddTransactionComponent },
    { path: 'reminders', component: RemindersComponent }
];


export const routing = RouterModule.forRoot(routes);

