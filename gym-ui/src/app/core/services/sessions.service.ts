import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { SessionCreateDto, SessionDto } from '../models/sessions.models';

@Injectable({ providedIn: 'root' })
export class SessionsService {
  private base = `${environment.apiUrl}/sessions`;

  constructor(private http: HttpClient) {}

  listAll(): Observable<SessionDto[]> {
    return this.http.get<SessionDto[]>(this.base);
  }
  create(dto: SessionCreateDto): Observable<SessionDto> {
    return this.http.post<SessionDto>(this.base, dto);
  }
  update(id: number, dto: SessionDto): Observable<SessionDto> {
    return this.http.put<SessionDto>(`${this.base}/${id}`, dto);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
  getById(id: number): Observable<SessionDto> {
    return this.http.get<SessionDto>(`${this.base}/${id}`);
  }
}
