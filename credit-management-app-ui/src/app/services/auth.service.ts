import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = `${environment.url}/auth`;
  
  constructor(private http: HttpClient) { }

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
    return localStorage.removeItem('authToken');
  }
}
