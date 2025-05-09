import { provideRouter, RouterModule, Routes } from '@angular/router';
import { CustomerListComponent } from './components/customer-list/customer-list.component';
import { importProvidersFrom, NgModule } from '@angular/core';
import { AddCustomerComponent } from './components/add-customer/add-customer.component';
import { TransactionListComponent } from './components/transaction-list/transaction-list.component';
import { AddTransactionComponent } from './components/add-transaction/add-transaction.component';
import { RemindersComponent } from './components/reminders/reminders.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// Import Angular Material Modules
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { TransactionsDataComponent } from './components/transactions-data/transactions-data.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuardService } from './services/auth.guard.service';
import { AuthInterceptorService } from './services/auth.interceptor.service';
import { SignupComponent } from './components/signup/signup.component';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { format } from 'date-fns';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { NgChartsModule } from 'ng2-charts';

export const routes: Routes = [
    { path: 'customers', component: CustomerListComponent , canActivate: [AuthGuardService]},
    { path: 'add-customer', component: AddCustomerComponent, canActivate: [AuthGuardService] },
    { path: 'transactions/:customerId', component: TransactionListComponent, canActivate: [AuthGuardService] },
    { path: 'add-transaction/:customerId', component: AddTransactionComponent, canActivate: [AuthGuardService] },
    { path: 'reminders', component: RemindersComponent, canActivate: [AuthGuardService] },
    { path: 'login', component: LoginComponent },
    { path: 'transactions', component: TransactionsDataComponent, canActivate: [AuthGuardService]},
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    { path: 'signup', component: SignupComponent },
    { path: 'dashboard', component: DashboardComponent }
];


export const routing = RouterModule.forRoot(routes);

bootstrapApplication(AppComponent, {
    providers: [
      provideHttpClient(withInterceptorsFromDi()),
      provideRouter(routes),
      provideHttpClient(),
      importProvidersFrom(
        MatInputModule,
        MatFormFieldModule,
        MatButtonModule,
        MatSelectModule,
        MatPaginatorModule,
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
            NgChartsModule  
      )
    ]
  });