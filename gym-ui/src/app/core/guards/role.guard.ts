import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { UserService } from '../services/user.service';
import { map } from 'rxjs/operators';
import { of } from 'rxjs';

export const roleGuard = (requiredRole: string): CanActivateFn => {
  return () => {
    const userService = inject(UserService);
    const router = inject(Router);

    return userService.myAuthorities().pipe(
      map(auths => {
        if (auths?.includes(`ROLE_${requiredRole}`)) return true;
        router.navigate(['/']);
        return false;
      })
    );
  };
};
