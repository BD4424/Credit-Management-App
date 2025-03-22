import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-signup',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  signupForm: FormGroup;
  errorMessage = '';
  successMessage = '';

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.signupForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.signupForm.valid) {
      const signupData = {
        userName: this.signupForm.value.email, // Map email to userName
        password: this.signupForm.value.password,
        role: 'SHOP_OWNER'
      }
      this.authService.register(signupData).subscribe({
        next: () => {
          this.successMessage = 'Registration successful! Redirecting to login...';
          setTimeout(() => this.router.navigate(['/login']), 2000);
        },
        error: (err) => {
          console.error("❌ Registration error:", err);
          this.errorMessage = err.error.message || 'Registration failed. Try again.';
        }
      });
    }
  }

  navigateToLogin(){
    this.router.navigate(['/login']);
  }
}
