import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { BookingCreateDto, BookingDto } from '../models/booking.models';

@Injectable({ providedIn: 'root' })
export class BookingsService {
  private base = `${environment.apiUrl}/bookings`;

  constructor(private http: HttpClient) {}

  create(dto: BookingCreateDto): Observable<BookingDto> {
    return this.http.post<BookingDto>(this.base, dto);
  }

  cancel(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  getById(id: number): Observable<BookingDto> {
    return this.http.get<BookingDto>(`${this.base}/${id}`);
  }

  listMine(): Observable<BookingDto[]> {
    return this.http.get<BookingDto[]>(`${this.base}/me`);
  }

  listBySession(sessionId: number): Observable<BookingDto[]> {
    return this.http.get<BookingDto[]>(`${this.base}/by-session/${sessionId}`);
  }

  listByGym(gymId: number): Observable<BookingDto[]> {
    return this.http.get<BookingDto[]>(`${this.base}/by-gym/${gymId}`);
  }

  countBySession(sessionId: number): Observable<number> {
    return this.http.get<number>(`${this.base}/count`, { params: { sessionId } as any });
  }
}
