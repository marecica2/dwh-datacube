import { Router } from "@angular/router";
import { getTestBed, TestBed } from "@angular/core/testing";
import { RouterTestingModule } from "@angular/router/testing";
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AuthService, Token, TokenUser, UserResponse } from "./auth.service";
import { User } from "../shared/user.model";
import { Tenant } from "./tenant/tenant.model";

const baseUrl = '/api/security';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let injector: TestBed;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([]),
      ],
      declarations: [],
      providers: [
        { provide: AuthService },
      ],
    });
    injector = getTestBed();
    httpMock = TestBed.inject(HttpTestingController);
    service = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  afterEach(() => {
    sessionStorage.clear();
  })

  it('should log in as user and be redirected to tenant default site', () => {
    const navigateSpy = spyOn(router, 'navigate');
    const tokenResponse = { access_token: 'token' } as Token;
    const userResponse = { username: 'user', roles: ['USER'] } as UserResponse;

    const user = new User();
    user.username = userResponse.username;
    user.roles = userResponse.roles;

    // service.tokenSubject.subscribe((token) => {
    //   expect(token).toEqual(tokenResponse);
    // });

    service.userSubject.subscribe((user) => {
      expect(user).toEqual(user);
    });

    service.login('user', 'password').subscribe((res: TokenUser) => {
      expect(res).toEqual({ token: tokenResponse, user: userResponse } as TokenUser);
      expect(navigateSpy.calls.mostRecent().args[0]).toEqual(['/auth/tenant']);
    });

    const request1 = httpMock.expectOne(`${baseUrl}/oauth/token`);
    request1.flush(tokenResponse);
    const request2 = httpMock.expectOne(`${baseUrl}/me`);
    request2.flush(userResponse);
    expect(2).toEqual(2);
  });

  it('should log in as user and be redirected to admin default site', () => {
    const tokenResponse = { access_token: 'token' } as Token;
    const userResponse = { username: 'admin', roles: ['ADMIN', 'USER'] } as UserResponse;
    const navigateSpy = spyOn(router, 'navigate');

    service.login('admin', 'password').subscribe(res => {
      expect(res).toEqual({ token: tokenResponse, user: userResponse });
      expect(navigateSpy.calls.mostRecent().args[0]).toEqual(['/iam']);
    });

    const request1 = httpMock.expectOne(`${baseUrl}/oauth/token`);
    request1.flush(tokenResponse);
    const request2 = httpMock.expectOne(`${baseUrl}/me`);
    request2.flush(userResponse);
  });

  it('should resolve with invalid user/password error', () => {
    const errorResp = { error: 'invalid_grant' };

    service.login('admin', 'password').subscribe(res => {
    }, error => {
      expect(error).toEqual('Invalid username or password');
    });

    const request1 = httpMock.expectOne(`${baseUrl}/oauth/token`);
    request1.flush(errorResp, { status: 401, statusText: 'Unauthorized' });
    expect(true).toBeTrue();
  });

  it('should resolve with general error', () => {
    const errorResp = {};

    service.login('admin', 'password').subscribe(res => {
    }, error => {
      expect(error).toEqual('Error occured');
    });

    const request1 = httpMock.expectOne(`${baseUrl}/oauth/token`);
    request1.flush(errorResp, { status: 500, statusText: 'Internal server error' });
    expect(true).toBeTrue();
  });

  it('should logout and redirect to login', () => {
    const navigateSpy = spyOn(router, 'navigate');
    service.userSubject.subscribe(user => {
      expect(user).toBeNull();
    });
    service.logout();
    expect(navigateSpy.calls.mostRecent().args[0]).toEqual(['/auth/login']);
  });

  it('should select tenant and redirect', () => {
    const navigateSpy = spyOn(router, 'navigate');
    service.selectTenant(new Tenant());
    expect(navigateSpy.calls.mostRecent().args[0]).toEqual(['/apps']);
  });
});
