import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RemindersService {

  private baseUrl = `${environment.url}/api/send-reminders`;

  constructor(private http: HttpClient) { }

  sendReminderEmailToCustomer(customerId: number): Observable<{ message: string }> {
      return this.http.post<any>(`${this.baseUrl}/sendIndividualReminder/${customerId}`, {});
    }
}
