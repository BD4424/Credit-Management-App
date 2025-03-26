import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  
  private baseUrl = `${environment.url}/transactions`;

  constructor(private http: HttpClient) { }

  getTransactionsByCustomer(customerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/allTransactions/${customerId}`)
  }

  addTransactionForCustomer(transactionData: any): Observable<any[]> {
    return this.http.post<any>(`${this.baseUrl}`, transactionData);
  }

  updateTransactionAsPaid(transactionId: number): Observable<any[]> {
    return this.http.put<any>(`${this.baseUrl}/updateTransaction/${transactionId}`, {})
  }

  allTransactions(): Observable<any[]> {
    return this.http.get<any>(`${this.baseUrl}/allTransactions`);
  }

  // getTransactions(fromDate?: string, toDate?: string, status?: string) {
  //   let params: any = {};
  // if (fromDate) params.fromDate = fromDate;
  // if (toDate) params.toDate = toDate;
  // if (status && status !== 'ALL') params.status = status;

  // return this.http.get<any>(`${this.baseUrl}/getFilteredTransactions`,params);
  // }

  getTransactions(fromDate?: string, toDate?: string, status?: string): Observable<any[]> {
    let params = new HttpParams();
    
    if (fromDate) {
      params = params.append('fromDate', fromDate);
    }
    
    if (toDate) {
      params = params.append('toDate', toDate);
    }
    
    if (status) {
      params = params.append('status', status);
    }
  
    return this.http.get<any[]>(`${this.baseUrl}/getFilteredTransactions`, { params });
  }
}
