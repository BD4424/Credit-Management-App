import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private baseUrl = `${environment.url}/customers`; // Backend API

  constructor(private http: HttpClient) { }

  getCustomers(): Observable<any> {
    return this.http.get(`${this.baseUrl}`);
  }

  addCustomer(customer: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}`, customer);
  }
}
