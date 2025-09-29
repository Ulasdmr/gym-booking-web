import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe, NgFor, NgIf } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { environment } from '../../../environments/environment';

interface BookingDto {
  id: number;
  sessionId: number;
  sessionName?: string;
  gymName?: string;
  startTime?: string;
  endTime?: string;
}

@Component({
  standalone: true,
  selector: 'app-booking',
  imports: [CommonModule, NgIf, NgFor, DatePipe, MatButtonModule, MatIconModule],
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.scss']
})
export class BookingComponent implements OnInit {
  private http = inject(HttpClient);

  bookings: BookingDto[] = [];
  loading = true;
  error: string | null = null;
  cancelBusy: number | null = null;

  ngOnInit(): void {
    this.fetch();
  }

  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    let h = new HttpHeaders({ 'Content-Type': 'application/json' });
    if (token) h = h.set('Authorization', `Bearer ${token}`);
    return h;
  }

  fetch() {
    this.loading = true; this.error = null;
    this.http.get<BookingDto[]>(
      `${environment.apiUrl}/bookings/me`,
      { headers: this.authHeaders() }
    ).subscribe({
      next: r => { this.bookings = r ?? []; this.loading = false; },
      error: e => {
        console.error('BOOKINGS LIST ERROR', e.status, e?.error || e);
        this.error = e?.error?.message || 'Rezervasyonlar yüklenemedi';
        this.loading = false;
      }
    });
  }

  cancel(id: number) {
    this.cancelBusy = id;
    this.http.delete(
      `${environment.apiUrl}/bookings/${id}`,
      { headers: this.authHeaders() }
    ).subscribe({
      next: () => { this.cancelBusy = null; this.fetch(); },
      error: e => {
        this.cancelBusy = null;
        console.error('BOOKING CANCEL ERROR', e.status, e?.error || e);
        alert(e?.error?.message || 'İptal başarısız');
      }
    });
  }
}
