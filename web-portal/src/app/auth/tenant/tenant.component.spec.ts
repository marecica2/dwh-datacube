import { of } from "rxjs";
import { Router } from "@angular/router";
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HarnessLoader } from "@angular/cdk/testing";
import { MatSelectHarness } from "@angular/material/select/testing";
import { MatOptionHarness } from "@angular/material/core/testing";
import { TenantComponent } from './tenant.component';
import { AuthService } from "../auth.service";

import { setupMaterialComponentTest } from "../../utils/testUtils";
import { Tenant } from "./tenant.model";
import { User } from "../../shared/user.model";

function prepareUser() {
  const tenant = new Tenant();
  tenant.id = "tenant-00001";
  tenant.name = "Test tenant";
  const user = new User();
  user.tenants = [tenant];
  return user;
}



describe('TenantComponent', () => {
  let fixture: ComponentFixture<any>;
  let component: TenantComponent;
  let router: Router;
  let loader: HarnessLoader;
  let authService: AuthService;

  beforeEach(async () => {

  });

  afterEach(() => {
    // document.querySelector('#app-tenant').remove()
  });

  it('should be able to select a tenant', async () => {

    class MockAuthService {
      public userSubject = of<User>(prepareUser())
      public selectTenant = jasmine.createSpy("selectTenant")
    }

    ({ fixture, loader } = await setupMaterialComponentTest({
      module: TenantComponent,
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
    spyOn(router, 'navigate')

    const select: MatSelectHarness = await loader.getHarness(MatSelectHarness);
    expect(component).toBeDefined();
    expect(select).toBeDefined();

    await select.open();
    const options: MatOptionHarness[] = await select.getOptions();
    const option = options[0];
    expect(options.length).toBe(1);
    expect(await option.getText()).toBe("Test tenant");

    await option.click();
    expect(authService.selectTenant).toHaveBeenCalled();

    component.ngOnDestroy();
  });
});
