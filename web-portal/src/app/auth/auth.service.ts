import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, Observable, throwError } from "rxjs";
import { catchError, map, switchMap, tap } from "rxjs/operators";
import { User } from "../shared/user.model";
import { ActivatedRoute, Router } from "@angular/router";
import { Tenant } from "../shared/tenant.model";

const baseUrl = '/api/security';
const CLIENT_ID = 'dwh-client';
const CLIENT_SECRET = 'secret';

export interface Token {
  access_token: string,
  token_type: string,
  refresh_token: string,
  expires_in: number,
  scope: string,
  jti: string,
}

export interface UserResponse {
  id: number,
  firstName: string,
  lastName: string,
  username: string,
  email: string,
  roles: string[],
  tenants: object[],
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  public userSubject = new BehaviorSubject<User>(null);
  public tokenSubject = new BehaviorSubject<Token>(null);
  private user: User;
  private selectedTenant: Tenant;
  private token: Token;

  constructor(
    private http: HttpClient,
    private router: Router,
    private activatedRoute: ActivatedRoute,
  ) {
    this.token = AuthService.getItem('token');
    if (this.token !== null) {
      this.tokenSubject.next(this.token);
    }
    this.user = AuthService.getItem('user');
    if (this.user !== null) {
      this.userSubject.next(this.user);
    }
  }

  public login(username: string, password: string): Observable<any> {
    return this.fetchToken(username, password)
      .pipe(
        switchMap((token: Token) => this.fetchUserInfo(token)),
        catchError(AuthService.handleError.bind(this)),
      );
  }

  public getToken(): string {
    return this.token.access_token;
  }

  public logout() {
    AuthService.deleteItem('token');
    AuthService.deleteItem('user');
    AuthService.deleteItem('tenant');
    this.userSubject.next(null);
    this.router.navigate(['/auth/login']);
  }

  public selectTenant(tenant: Tenant) {
    const route = this.activatedRoute.snapshot;
    const { queryParams: { redirectUri } } = route;

    this.selectedTenant = tenant;
    AuthService.setItem('tenant', tenant);

    if (redirectUri) {
      window.location.href = redirectUri;
    } else {
      this.router.navigate(['/apps']);
    }
  }

  private fetchToken(username: string, password: string): Observable<Token> {
    const formData = new FormData();
    formData.append('username', username);
    formData.append('password', password);
    formData.append('grant_type', 'password');
    const headers = {
      'Authorization': `Basic ${btoa(`${CLIENT_ID}:${CLIENT_SECRET}`)}`,
    };
    return this.http
      .post<Token>(`${baseUrl}/oauth/token`, formData, { headers }).pipe(
        tap((token: Token) => {
          this.token = token;
          this.tokenSubject.next(token);
          AuthService.setItem('token', token);
        }),
      )
  }

  private fetchUserInfo(token: Token): Observable<any> {
    return this.http
      .get<UserResponse>(`${baseUrl}/me`)
      .pipe(
        tap((usr: UserResponse) => {
          AuthService.setItem('user', usr);
          const user = new User();
          user.id = usr.id;
          user.firstName = usr.firstName;
          user.lastName = usr.lastName;
          user.email = usr.email;
          user.roles = usr.roles;
          user.tenants = usr.tenants;
          user.token = token.access_token;
          user.expiresIn = token.expires_in;
          user.refreshToken = token.refresh_token;
          this.userSubject.next(user);
        }),
        map((user: UserResponse) => ({
          token, user,
        })),
        catchError(AuthService.handleError.bind(this)),
      );
  }

  private static handleError(error) {
    console.log(error);
    let errorMessage = 'Error occured';
    if (!error.error || !error.error.error) {
      return throwError(errorMessage);
    }
    switch (error.error.error) {
      case 'invalid_grant':
        errorMessage = 'Invalid username or password'
    }
    return throwError(errorMessage);
  };

  private static setItem(key: string, object: Object) {
    sessionStorage.setItem(key, JSON.stringify(object));
  }

  private static deleteItem(key: string) {
    sessionStorage.removeItem(key);
  }

  private static getItem(key: string): any {
    return JSON.parse(sessionStorage.getItem(key));
  }
}
