import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class KPIMetricsService {

  constructor(private http: HttpClient) { }
  
    private baseUrl = `${environment.url}/analytics`;

  getKPIMetrics(): Observable<any[]> {
      return this.http.get<any[]>(`${this.baseUrl}/kpis`);
    }
}
