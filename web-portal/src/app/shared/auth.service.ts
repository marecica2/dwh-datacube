import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, concat, Observable, Subject } from "rxjs";
import { catchError, map, mergeMap, switchMap, tap } from "rxjs/operators";
import { throwError } from "rxjs";
import { User } from "./user.model";
import { ActivatedRoute, Router } from "@angular/router";
import { Tenant } from "./tenant.model";

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
  private user: User;
  private token: Token;

  constructor(
    private http: HttpClient,
    private router: Router,
    private activatedRoute: ActivatedRoute,
  ) {
    this.user = AuthService.getItem('user');
    this.token = AuthService.getItem('token');
    if (this.user !== null)
      this.userSubject.next(this.user);
  }

  login(username: string, password: string): Observable<string> {
    return this.fetchToken(username, password)
      .pipe(
        mergeMap(this.fetchUserInfo.bind(this)),
        map(() => {
          return 'finished'
        }),
        catchError(AuthService.handleError.bind(this)),
      );
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
        tap((auth: Token) => {
          this.token = auth;
          AuthService.setItem('token', auth);
        }),
      )
  }

  public isAuthenticated(): boolean {
    return AuthService.getItem('token') !== null;
  }

  protected getToken(): string {
    return this.token.access_token;
  }

  public logout() {
    AuthService.deleteItem('token');
    AuthService.deleteItem('user');
    AuthService.deleteItem('tenant');
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }

  public selectTenant(tenant: Tenant) {
    const route = this.activatedRoute.snapshot;
    const { queryParams: { redirectUri } } = route;
    if (redirectUri) {
      window.location.href = redirectUri;
    } else {
      this.router.navigate(['/apps']);
    }
  }

  private fetchUserInfo(): Observable<any> {
    return this.http
      .get<UserResponse>(`${baseUrl}/me`)
      .pipe(
        tap((usr: UserResponse) => {
          const token: Token = AuthService.getItem('token');
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
