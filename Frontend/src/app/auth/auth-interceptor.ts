import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import {catchError} from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

   constructor(private router:Router) {}

   intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

     return next.handle(request).pipe(catchError(x=> this.handleAuthError(x)));

   }

     private handleAuthError(err: HttpErrorResponse): Observable<any> {
        if (err.status === 403) {
           this.router.navigateByUrl(`/error`);
        }

        return throwError(err);
     }
}
