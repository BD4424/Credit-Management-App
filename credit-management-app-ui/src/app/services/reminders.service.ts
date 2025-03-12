import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RemindersService {

  private baseUrl = 'http://localhost:8080/api/send-reminders';

  constructor(private http: HttpClient) { }

  sendReminderEmailToCustomer(customerId: number): Observable<any[]> {
      return this.http.post<any>(`${this.baseUrl}/sendIndividualReminder/${customerId}`, {});
    }
}
