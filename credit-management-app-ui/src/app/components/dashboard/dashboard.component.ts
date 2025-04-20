import { Component, OnInit } from '@angular/core';
import { ChartOptions, ChartType, ChartData } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NgChartsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  public chartOptions: ChartOptions = {
    responsive: true,
  };

  public chartType: ChartType = 'bar';

  public chartLabels: string[] = ['January', 'February', 'March', 'April', 'May', 'June'];

  public chartData: ChartData<'bar'> = {
    labels: ['January', 'February', 'March', 'April', 'May', 'June'],
    datasets: [
      {
        data: [65, 59, 80, 81, 56, 55],
        label: 'Transactions'
      }
    ]
  };
  

  constructor() { }

  ngOnInit(): void {
  }

}
