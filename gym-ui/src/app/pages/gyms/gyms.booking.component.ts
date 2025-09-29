import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { MatButtonModule } from '@angular/material/button';
import { environment } from '../../../environments/environment';

interface GymDto {
  id: number;
  name: string;
  address?: string;
  description?: string;
}

@Component({
  standalone: true,
  selector: 'app-gyms',
  imports: [CommonModule, NgIf, NgFor, MatButtonModule],
  templateUrl: './gyms.booking.component.html',
  styleUrls: ['./gyms.booking.component.scss']
})
export class GymsComponent implements OnInit {
  private http = inject(HttpClient);

  gyms: GymDto[] = [];
  loading = true;
  error: string | null = null;

  ngOnInit(): void { this.fetch(); }

  fetch() {
    this.loading = true; this.error = null;
    this.http.get<GymDto[]>(`${environment.apiUrl}/gyms/all`).subscribe({
      next: r => { this.gyms = r ?? []; this.loading = false; },
      error: e => { this.error = e?.error?.message || 'Salonlar yüklenemedi'; this.loading = false; }
    });
  }
}
