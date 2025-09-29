import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  standalone: true,
  selector: 'app-main',
  imports: [CommonModule, MatCardModule, MatButtonModule],
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {
  private http = inject(HttpClient);
  private router = inject(Router);

  roles: string[] = [];
  loading = true;

  ngOnInit(): void {
    this.http.get<string[]>(`${environment.apiUrl}/users/me/authorities`).subscribe({
      next: (res) => { this.roles = res || []; this.loading = false; },
      error: () => { this.roles = []; this.loading = false; }
    });
  }

  get isAdmin() {
    return this.roles.includes('ROLE_ADMIN') || this.roles.includes('ADMIN');
  }

  goto(path: string) { this.router.navigate([path]); }
  logout() { localStorage.removeItem('token'); this.router.navigate(['/auth/login']); }
}
