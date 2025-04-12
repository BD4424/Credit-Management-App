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
  template: `
    <mat-card>
      <mat-card-header>
        <h3>{{ title }}</h3>
      </mat-card-header>
      <mat-card-content>
        <div class="kpi-value">
          {{ value }}
          <mat-icon *ngIf="trend === 'up'" color="warn">arrow_upward</mat-icon>
          <mat-icon *ngIf="trend === 'down'" color="primary">arrow_downward</mat-icon>
        </div>
      </mat-card-content>
    </mat-card>
  `,
  styles: [`
    mat-card { 
      width: 200px; 
      margin: 8px;
    }
    .kpi-value { 
      font-size: 24px; 
      display: flex; 
      align-items: center; 
      gap: 8px;
    }
  `]
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

