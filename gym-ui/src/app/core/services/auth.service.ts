import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { AuthResponse, UserLoginDto, UserRegisterDto } from '../models/auth.models';
import { Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private base = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  login(dto: UserLoginDto): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.base}/login`, dto).pipe(
      tap(res => localStorage.setItem('token', res.token))
    );
  }

  register(dto: UserRegisterDto): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.base}/register`, dto).pipe(
      tap(res => localStorage.setItem('token', res.token))
    );
  }

  logout() { localStorage.removeItem('token'); }

  get token(): string | null { return localStorage.getItem('token'); }

  isLoggedIn(): boolean { return !!this.token; }
}
