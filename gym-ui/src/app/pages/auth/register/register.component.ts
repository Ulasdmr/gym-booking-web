import { Component } from '@angular/core';
import { CommonModule, formatDate } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

/** password === confirmPassword kontrolü */
function matchPasswords(group: AbstractControl): ValidationErrors | null {
  const p = group.get('password')?.value;
  const c = group.get('confirmPassword')?.value;
  return p && c && p !== c ? { passwordMismatch: true } : null;
}

@Component({
  standalone: true,
  selector: 'app-register',
  imports: [
    CommonModule, ReactiveFormsModule, RouterLink,
    MatFormFieldModule, MatInputModule, MatButtonModule,
    MatSelectModule, MatDatepickerModule, MatNativeDateModule,
    MatSnackBarModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  form!: FormGroup;
  loading = false;
  error: string | null = null;

  userTypes = ['USER', 'ADMIN']; // drop-down

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private sb: MatSnackBar
  ) {
    this.form = this.fb.group(
      {
        firstName: ['', [Validators.required, Validators.maxLength(60)]],
        lastName: ['', [Validators.required, Validators.maxLength(60)]],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', [Validators.required]],
        birthDate: [null, [Validators.required]],   // Date objesi
        userType: ['USER', [Validators.required]],
      },
      { validators: matchPasswords }
    );
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      if (this.form.errors?.['passwordMismatch']) {
        this.error = 'Şifreler uyuşmuyor';
      }
      return;
    }

    this.loading = true;
    this.error = null;

    // Doğru payload: birthDate → YYYY-MM-DD
    const v = this.form.value;
    const birthDate = v.birthDate
      ? formatDate(v.birthDate, 'yyyy-MM-dd', 'en-CA')   // 2002-12-14
      : null;

    const dto = {
      firstName: v.firstName,
      lastName: v.lastName,
      email: v.email,
      password: v.password,
      confirmPassword: v.confirmPassword,
      birthDate,
      userType: v.userType
    };

    this.auth.register(dto as any).subscribe({
      next: () => {
        this.sb.open('Kayıt başarılı! 👋', 'Tamam', { duration: 1800 });
        this.router.navigate(['/main']); // token zaten kaydedildi → ana sayfa
      },
      error: (e: any) => {
        this.error = e?.error?.message ?? 'Kayıt başarısız';
        this.loading = false;
      }
    });
  }
}
