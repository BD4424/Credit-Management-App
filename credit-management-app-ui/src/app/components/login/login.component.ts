import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule  } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';

  constructor(private fb: FormBuilder, private authSerice: AuthService, private router: Router){
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    })
  }

  onSubmit(){
    if(this.loginForm.valid){
      this.authSerice.login(this.loginForm.value).subscribe({
        next: (response) => {
          console.log("API responded:", response);
          this.authSerice.saveToken(response.token);
          console.log("Login success, navigating...");
          this.router.navigate(['/customers']);
        },
        error: (err) => {
          console.error("Login API error:", err);
          this.errorMessage = 'Invalid email or password';
        }
      });
    }
  }

}
