import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';

import { AuthService } from './auth.service';
import { User } from "../shared/user.model";

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  public user: User;

  constructor(private authService: AuthService, private router: Router) {
    this.authService.userSubject.subscribe(user => {
      this.user = user;
    });
  }

  hasAllRoles(roles, requiredRoles) {
    return requiredRoles.every((i => v => i = roles.indexOf(v, i) + 1)(0));
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    router: RouterStateSnapshot
  ): boolean | UrlTree | Promise<boolean | UrlTree> | Observable<boolean | UrlTree> {
    const requiredRoles = route.data.roles as Array<string>;
    const isAuth = !!this.user;
    if (isAuth && this.hasAllRoles(this.user.roles, requiredRoles)) {
      return true;
    }
    return this.router.createUrlTree(['/auth/login']);
  }
}
