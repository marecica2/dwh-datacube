import { of } from "rxjs";
import { Router } from "@angular/router";
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HarnessLoader } from "@angular/cdk/testing";
import { CrudTableComponent } from "./crud-table.component"

import { setupMaterialComponentTest } from "../../utils/testUtils";
import { ColumnDefinition, CrudResource, MultiSelectColumn, SimpleColumn } from "./crudRepositoryApi";
import { RestService } from "@lagoshny/ngx-hal-client";
import { Component } from "@angular/core";
import { MatTableHarness } from "@angular/material/table/testing";


class Role extends CrudResource {
  public id: number;
  public name: string;

  getIdentity(): any {
  }

  getIdentityKey(): string {
    return "id";
  }

  getRelations(): string[] {
    return [];
  }
}

class User extends CrudResource {
  public id: number;
  public firstName: string;
  public lastName: string;
  public roles: Role[];

  getIdentity(): any {
  }

  getIdentityKey(): string {
    return "id";
  }

  getRelations(): string[] {
    return ['roles'];
  }
}

const userRole: Role = new Role();
userRole.id = 1;
userRole.name = 'USER';
userRole._links = { self: { href: '/roles/1' } }

const adminRole: Role = new Role();
adminRole.id = 2;
adminRole.name = 'ADMIN';
adminRole._links = { self: { href: '/roles/2' } }

const otherRole: Role = new Role();
otherRole.id = 3;
otherRole.name = 'OTHER';
otherRole._links = { self: { href: '/roles/3' } }

const user: User = new User();
user.id = 1;
user.firstName = 'Test';
user.lastName = 'Test';
user.roles = [userRole, adminRole];
user._links = { self: { href: '/users/1' } }

@Component({
  selector: 'host-component',
  template: `
    <app-crud-table-component
      [crudService]="mockUserService"
      [columnDefinition]="columnDefinition"
      [relation]="'users'"
      [editable]="true"
      [modelType]="userRef"
    >
    </app-crud-table-component>`,
})
class TestHostComponent {
  userRef = User;

  public mockUserService: jasmine.SpyObj<RestService<User>> = jasmine.createSpyObj<RestService<User>>('RestService', {
    patch: of(null),
    create: of(null),
    getAll: of([user]),
    totalElement: 1,
    get: of(user),
  });

  public mockRoleService: jasmine.SpyObj<RestService<Role>> = jasmine.createSpyObj<RestService<Role>>('RestService', {
    getAll: of([userRole, adminRole, otherRole]),
  });

  public columnDefinition: ColumnDefinition = {
    firstName: new SimpleColumn(
      'First name',
    ),
    lastName: new SimpleColumn(
      'Last name',
    ),
    roles: new MultiSelectColumn(
      'name',
      this.mockRoleService,
      'name',
    )
  }
}

describe('CrudTableComponent', () => {
  let fixture: ComponentFixture<any>;
  let component: TestHostComponent;
  let router: Router;
  let loader: HarnessLoader;


  beforeEach(async () => {
    ({ fixture, loader } = await setupMaterialComponentTest({
      module: TestHostComponent,
      imports: [],
      declarations: [CrudTableComponent],
      providers: [],
    }));

    router = TestBed.inject(Router);
    component = fixture.componentInstance;
  });

  afterEach(() => {

  });

  it('should display crud table', async () => {
    fixture.detectChanges();
    expect(true).toBeTrue();
    const table: MatTableHarness = await loader.getHarness(MatTableHarness);
    expect(component.mockUserService.getAll).toHaveBeenCalled();
    const matRowHarnesses = await table.getRows();
    const cells = await matRowHarnesses[0].getCells();
    expect(await cells[0].getText()).toEqual("Test");
    expect(await cells[1].getText()).toEqual("Test");
    expect(await cells[2].getText()).toEqual("USERADMIN");
  });
});
