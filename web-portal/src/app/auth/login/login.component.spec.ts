import { Observable, of, throwError } from "rxjs";
import { Router } from "@angular/router";
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HarnessLoader } from "@angular/cdk/testing";
import { AuthService } from "../auth.service";

import { setupMaterialComponentTest } from "../../utils/testUtils";
import { User } from "../../shared/user.model";
import { LoginComponent } from "./login.component";
import { MatButtonHarness } from "@angular/material/button/testing";
import { FormsModule } from "@angular/forms";
import { MatInputHarness } from "@angular/material/input/testing";
import { MatSnackBarHarness } from "@angular/material/snack-bar/testing";

describe('LoginComponent', () => {
  let fixture: ComponentFixture<any>;
  let component: LoginComponent;
  let router: Router;
  let loader: HarnessLoader;
  let rootLoader: HarnessLoader;
  let authService: AuthService;

  beforeEach(async () => {
    class MockAuthService {
      public login = () => {
      }
    }

    ({ fixture, loader, rootLoader } = await setupMaterialComponentTest({
      module: LoginComponent,
      imports: [FormsModule],
      declarations: [],
      providers: [
        { provide: AuthService, useClass: MockAuthService }
      ],
    }));

    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    component = fixture.componentInstance;
  });

  afterEach(() => {
  });

  it('should be able to login', async () => {
    spyOn(authService, 'login').and.returnValue(of(true));
    fixture.detectChanges();

    const user: MatInputHarness = await loader.getHarness<MatInputHarness>(
      MatInputHarness.with({ selector: '[name=username]' }));
    await user.setValue('user');

    const password: MatInputHarness = await loader.getHarness<MatInputHarness>(
      MatInputHarness.with({ selector: '[name=password]' }));
    await password.setValue('secret');

    const submit: MatButtonHarness = await loader.getHarness(MatButtonHarness);
    await submit.click();

    expect(authService.login).toHaveBeenCalledWith('user', 'secret');
  });

  it('should fail to login', async (done) => {
    spyOn(authService, 'login').and.returnValue(throwError({ message: 'Invalid username or password', status: 401 }));
    fixture.detectChanges();

    const user: MatInputHarness = await loader.getHarness<MatInputHarness>(
      MatInputHarness.with({ selector: '[name=username]' }));
    await user.setValue('user');

    const password: MatInputHarness = await loader.getHarness<MatInputHarness>(
      MatInputHarness.with({ selector: '[name=password]' }));
    await password.setValue('secret');

    const submit: MatButtonHarness = await loader.getHarness(MatButtonHarness);
    await submit.click();

    expect(component.error).toEqual({ message: 'Invalid username or password', status: 401 })
    done();
  });
});
