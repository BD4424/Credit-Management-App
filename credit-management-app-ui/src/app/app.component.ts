import { ChangeDetectorRef, Component } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';
import { NgIf } from '@angular/common';
import { ThemeToggleComponent } from './components/theme-toggle/theme-toggle.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterModule, NgIf, ThemeToggleComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'credit-management-app-ui';

  constructor(private authService: AuthService, private router: Router, private cdRef: ChangeDetectorRef) {}

  isLoggedIn(): boolean {
    return this.authService.getToken() !== null; // Check if token exists
  }

  logout() {
    this.authService.logOut();
    this.cdRef.detectChanges();
    this.router.navigate(['/login']);
  }
}
