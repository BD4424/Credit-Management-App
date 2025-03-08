import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private baseUrl = 'http://localhost:8080/customers'; // Backend API

  constructor(private http: HttpClient) { }

  getCustomers(): Observable<any> {
    return this.http.get(`${this.baseUrl}`);
  }
}
