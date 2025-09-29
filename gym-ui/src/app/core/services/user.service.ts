import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  private base = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  myAuthorities(): Observable<string[]> {
    return this.http.get<string[]>(`${this.base}/me/authorities`);
  }

  getById(id: number) {
    return this.http.get(`${this.base}/${id}`);
  }
}
