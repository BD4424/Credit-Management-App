import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';
import { importProvidersFrom } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';

bootstrapApplication(AppComponent, {
  providers: [provideRouter(routes),
    provideHttpClient(),
    importProvidersFrom(ReactiveFormsModule) 
  ]
}).catch((err) => console.error(err));
