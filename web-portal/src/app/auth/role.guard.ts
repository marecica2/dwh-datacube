import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';

import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {
  }

  hasAllRoles(roles, requiredRoles) {
    return requiredRoles.every((i => v => i = roles.indexOf(v, i) + 1)(0));
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    router: RouterStateSnapshot
  ): boolean | UrlTree | Promise<boolean | UrlTree> | Observable<boolean | UrlTree> {
    const requiredRoles = route.data.roles as Array<string>;
    return this.authService.userSubject.pipe(
      take(1),
      map(user => {
        const isAuth = !!user;
        if (isAuth && this.hasAllRoles(user.roles, requiredRoles)) {
            return true;
        }
        return this.router.createUrlTree(['/auth/login']);
      })
    );
  }
}
