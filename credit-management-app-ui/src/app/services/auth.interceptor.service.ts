import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor{

  constructor(private authService: AuthService, private router: Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = this.authService.getToken();
    let clonedReq = req;
    if (token) {
      clonedReq = req.clone({
        setHeaders: { Authorization: `Bearer ${token}`}
      });
    }
    return next.handle(clonedReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 || error.status === 403) { // Unauthorized (Token Expired)
          console.warn('Token expired. Redirecting to login...');
          this.authService.logOut(); // Clear token from storage
          this.router.navigate(['/login']); // Redirect to login
        }
        return throwError(() => error);
      })
    );
  }
  
}
