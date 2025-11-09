import { CommonModule } from "@angular/common";
import { HttpClient } from "@angular/common/http";
import { Component, Input } from "@angular/core";
import { MatCardModule } from "@angular/material/card";
import { MatIconModule } from "@angular/material/icon";
import { Observable } from "rxjs";
import { environment } from "../../../../environments/environment";

@Component({
  selector: 'app-kpi-card',
  standalone: true,
  imports: [MatCardModule, MatIconModule, CommonModule],
  templateUrl: './kpi-card.component.html',
  styleUrls: ['./kpi-card.component.css']
})
export class KpiCardComponent {
  @Input() title!: string;
  @Input() value!: string | number;
  @Input() trend?: 'up' | 'down';

  constructor(private http: HttpClient) { }

  private baseUrl = `${environment.url}/analytics`;

  getKPIMetrics(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/kpis`);
  }

}

