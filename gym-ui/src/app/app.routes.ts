import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  
  { path: 'auth/login', loadComponent: () => import('./pages/auth/login/login.component').then(m => m.LoginComponent) },
  { path: 'auth/register', loadComponent: () => import('./pages/auth/register/register.component').then(m => m.RegisterComponent) },
  { path: 'register', redirectTo: 'auth/register', pathMatch: 'full' },

  
  { path: 'main', loadComponent: () => import('./pages/main/main.component').then(m => m.MainComponent), canActivate: [authGuard] },

  
  { path: 'sessions', loadComponent: () => import('./pages/sessions/sessions.component').then(m => m.SessionsComponent), canActivate: [authGuard] },
  { path: 'booking',  loadComponent: () => import('./pages/bookings/booking.component').then(m => m.BookingComponent),  canActivate: [authGuard] },
 


  
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' },
  { path: '**', redirectTo: 'auth/login' }
];
