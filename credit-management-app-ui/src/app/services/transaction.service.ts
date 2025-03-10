import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private baseUrl = 'http://localhost:8080/transactions';

  constructor(private http: HttpClient) { }

  getTransactionsByCustomer(customerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/pendingTransactions/${customerId}`)
  }

  addTransactionForCustomer(transactionData: any): Observable<any[]> {
    return this.http.post<any>(`${this.baseUrl}`, transactionData);
  }
}
