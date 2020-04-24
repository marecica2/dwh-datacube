import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';
import { of } from "rxjs";
import { User } from "../../../shared/user.model";
import { setupMaterialComponentTest } from "../../../utils/testUtils";
import { AuthService } from "../../../auth/auth.service";
import { Router } from "@angular/router";
import { HarnessLoader } from "@angular/cdk/testing";
import { Tenant } from "../../../auth/tenant/tenant.model";
import { MatButtonHarness } from "@angular/material/button/testing";

const tenant = new Tenant();
tenant.id = "tenant-00001";
tenant.name = "Test tenant";

const user = new User();
user.firstName = 'test';
user.lastName = 'test';
user.tenants = [tenant];


describe('HeaderComponent', () => {

  let fixture: ComponentFixture<any>;
  let component: HeaderComponent;
  let router: Router;
  let loader: HarnessLoader;
  let authService: AuthService;

  it('should render header', async () => {
    class MockAuthService {
      userSubject = of<User>(user);
      logout = jasmine.createSpy("logout")
    }

    ({ fixture, loader } = await setupMaterialComponentTest({
      module: HeaderComponent,
      imports: [],
      declarations: [],
      providers: [
        { provide: AuthService, useClass: MockAuthService }
      ],
    }));


    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    component = fixture.componentInstance;

    fixture.detectChanges();

    expect(component.isAuthenticated).toBeTrue();

    const btn: MatButtonHarness = await loader.getHarness<MatButtonHarness>(
      MatButtonHarness.with({ selector: `[aria-label="${user.firstName} ${user.lastName}"]` }));
    expect(btn).toBeDefined();

    component.onLogout();
    component.ngOnDestroy();
  });
});
