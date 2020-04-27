import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, } from '@angular/common/http/testing';
import { RouterTestingModule } from "@angular/router/testing";
import { AuthService } from "./auth.service";
import { GuardsCheckEnd, Router, Routes } from "@angular/router";
import { RoleGuard } from "./role.guard";
import { AppsComponent } from "../components/pages/apps/apps.component";
import { User } from "../shared/user.model";

const routes: Routes = [
  {
    path: 'restricted',
    component: AppsComponent,
    canActivate: [RoleGuard], data: { roles: ['admin'] },
  },
  {
    path: 'auth/login',
    component: AppsComponent,
  },
];

describe(`RoleGuard`, () => {
  let authService: AuthService;
  let httpMock: HttpTestingController;
  let router: Router;
  let guard: RoleGuard;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule.withRoutes(routes),
      ],
      providers: [
        RoleGuard,
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    guard = TestBed.inject(RoleGuard);
    authService = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  // TODO
  it('should not allow user to access admin protected route', () => {
    const spy = spyOn(router, 'createUrlTree').and.callThrough();

    const user = new User();
    user.username = 'user';
    user.roles = ['USER'];

    authService.userSubject.next(user);
    router.navigateByUrl('/restricted');
    expect(spy.calls.mostRecent()).toBeUndefined();
  });

});
