import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { GymCreateDto, GymDto } from '../models/gym.models';

@Injectable({ providedIn: 'root' })
export class GymService {
  private base = `${environment.apiUrl}/gyms`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<GymDto[]> {
    return this.http.get<GymDto[]>(`${this.base}/all`);
  }

  create(dto: GymCreateDto): Observable<GymDto> {
    return this.http.post<GymDto>(this.base, dto);
  }

  getById(id: number): Observable<GymDto> {
    return this.http.get<GymDto>(`${this.base}/${id}`);
  }
}
