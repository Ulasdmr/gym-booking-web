import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe, NgFor, NgIf } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; // ngModel için
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { environment } from '../../../environments/environment';

type WorkoutType = 'PILATES' | 'BOX' | 'YOGA' | 'FITNESS' | 'CRUNCH' | 'ZUMBA';

interface SessionDto {
  id: number;
  type?: WorkoutType;
  title?: string;
  description?: string;
  startTime?: string;  // ISO (YYYY-MM-DDTHH:mm:ss)
  endTime?: string;
  gymId?: number;
  gymName?: string;
  capacity?: number;
}

@Component({
  standalone: true,
  selector: 'app-sessions',
  imports: [
    CommonModule, NgIf, NgFor, DatePipe, FormsModule,
    RouterModule,
    MatButtonModule, MatIconModule,
    MatFormFieldModule, MatSelectModule, MatInputModule
  ],
  templateUrl: './sessions.component.html',
  styleUrls: ['./sessions.component.scss']
})
export class SessionsComponent implements OnInit {
  private http = inject(HttpClient);

  sessions: SessionDto[] = [];
  loading = true;
  error: string | null = null;

  bookingBusy: number | null = null;
  createBusy = false;
  deleteBusy: number | null = null;
  userIsAdmin = false;

  // Seans oluşturma için
  sessionTypes: WorkoutType[] = ['PILATES','BOX','YOGA','FITNESS','CRUNCH','ZUMBA'];
  selectedType: WorkoutType = 'PILATES';

  // ID ile rezervasyon alanları
  byIdsSessionId: number | null = null;
  byIdsGymId: number | null = null;
  byIdsBooking: string = this.defaultLocalDateTime();

  ngOnInit(): void {
    this.fetch();
    this.fetchAuthorities();
  }

  // ---- Helpers --------------------------------------------------------------
  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    let h = new HttpHeaders({ 'Content-Type': 'application/json' });
    if (token) h = h.set('Authorization', `Bearer ${token}`);
    return h;
  }

  private toBackendIsoFromDate(d: Date): string {
    const pad = (n: number) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
  }

  private defaultLocalDateTime(): string {
    const d = new Date();
    const pad = (n: number) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
  }

  private fromLocalDatetimeToBackendIso(dt: string): string {
    return dt && dt.length === 16 ? `${dt}:00` : dt;
  }

  // ---- API calls ------------------------------------------------------------
  fetch() {
    this.loading = true;
    this.error = null;
    this.http.get<SessionDto[]>(`${environment.apiUrl}/sessions`, { headers: this.authHeaders() })
      .subscribe({
        next: res => { this.sessions = res ?? []; this.loading = false; },
        error: e => {
          console.error('SESSIONS LIST ERROR', e.status, e?.error || e);
          this.error = e?.error?.message || e?.message || 'Seanslar yüklenemedi';
          this.loading = false;
        }
      });
  }

  private fetchAuthorities() {
    this.http.get<string[]>(`${environment.apiUrl}/users/me/authorities`, { headers: this.authHeaders() })
      .subscribe({
        next: roles => this.userIsAdmin = !!roles?.some(r => r === 'ROLE_ADMIN' || r === 'ADMIN'),
        error: () => { this.userIsAdmin = false; }
      });
  }

  createSession(type: WorkoutType = this.selectedType, startOffsetMin = 60, durationMin = 120) {
    this.createBusy = true;
    const now = new Date();
    const startDate = new Date(now.getTime() + startOffsetMin * 60 * 1000);
    const endDate   = new Date(startDate.getTime() + durationMin * 60 * 1000);

    const payload = {
      type,
      startTime: this.toBackendIsoFromDate(startDate),
      endTime:   this.toBackendIsoFromDate(endDate),
      capacity: 20,
      gymId: 1
    };

    this.http.post(`${environment.apiUrl}/sessions`, payload, { headers: this.authHeaders() })
      .subscribe({
        next: () => { this.createBusy = false; this.fetch(); },
        error: e => {
          console.error('SESSION CREATE ERROR', e.status, e?.error || e);
          this.createBusy = false;
          alert(e?.error?.message || e?.message || 'Seans oluşturma başarısız');
        }
      });
  }

  createZumbaExample() {
    this.createSession('ZUMBA', 60, 120);
  }

  // --- Rezervasyon (liste üzerinden) -----------------------------------------
  book(sessionId: number) {
    this.bookingBusy = sessionId;
    const session = this.sessions.find(s => s.id === sessionId);
    if (!session || !session.gymId) {
      this.bookingBusy = null;
      alert('Seans veya gymId bulunamadı.');
      return;
    }
    const payload = {
      sessionId,
      gymId: session.gymId,
      bookingDate: this.toBackendIsoFromDate(new Date())
    };
    this.http.post(`${environment.apiUrl}/bookings`, payload, { headers: this.authHeaders() })
      .subscribe({
        next: () => { this.bookingBusy = null; this.fetch(); },
        error: e => {
          this.bookingBusy = null;
          console.error('BOOKING ERROR', e.status, e?.error || e);
          alert(`Rezervasyon başarısız [${e.status}]: ${e?.error?.message || e.message || 'Bilinmeyen hata'}`);
        }
      });
  }

  // --- Rezervasyon (ID girerek) ---------------------------------------------
  bookByIds() {
    if (!this.byIdsSessionId || !this.byIdsGymId) {
      alert('Session ID ve Gym ID giriniz.');
      return;
    }
    this.bookingBusy = -1;
    const payload = {
      sessionId: this.byIdsSessionId,
      gymId: this.byIdsGymId,
      bookingDate: this.fromLocalDatetimeToBackendIso(this.byIdsBooking)
    };
    this.http.post(`${environment.apiUrl}/bookings`, payload, { headers: this.authHeaders() })
      .subscribe({
        next: () => { this.bookingBusy = null; this.fetch(); },
        error: e => {
          this.bookingBusy = null;
          console.error('BOOKING BY IDS ERROR', e.status, e?.error || e);
          alert(`Rezervasyon başarısız [${e.status}]: ${e?.error?.message || e.message || 'Bilinmeyen hata'}`);
        }
      });
  }

  // --- Silme (ADMIN) --------------------------------------------------------
  deleteSession(id: number) {
    if (!this.userIsAdmin) { alert('Bu işlem için yetkin yok.'); return; }
    if (!confirm('Bu seansı silmek istediğine emin misin?')) return;

    this.deleteBusy = id;
    this.http.delete(`${environment.apiUrl}/sessions/${id}`, { headers: this.authHeaders() })
      .subscribe({
        next: () => { this.deleteBusy = null; this.fetch(); },
        error: e => {
          this.deleteBusy = null;
          console.error('SESSION DELETE ERROR', e.status, e?.error || e);
          alert(e?.error?.message || 'Silme başarısız (yetki veya iş kuralı).');
        }
      });
  }
}
