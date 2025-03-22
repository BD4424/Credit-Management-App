import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = `${environment.url}/auth`;
  
  constructor(private http: HttpClient, private router: Router) { }

  login(credentials: {email: string; password: string}): Observable<any> {
    console.log("Inside login");
    return this.http.post(`${this.baseUrl}/login`,credentials);
  }

  saveToken(token: string){
    console.log("Inside save token");
    localStorage.setItem('authToken', token);
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logOut() {
    localStorage.removeItem('authToken');
    this.router.navigate(['/login']).then(() => {
      window.location.reload(); // Ensures UI updates properly
    });
  }

  register(userData: {userName: string; password: string; role: string }): Observable<any> {
    console.log("Register new user");
    return this.http.post(`${this.baseUrl}/register`,userData);
  }
}
