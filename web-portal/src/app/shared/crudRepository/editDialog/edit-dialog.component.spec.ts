import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RestService } from "@lagoshny/ngx-hal-client";
import { NgModule } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { OverlayContainer } from "@angular/cdk/overlay";
import { BrowserDynamicTestingModule } from "@angular/platform-browser-dynamic/testing";
import { AngularMaterialModule } from "../../angular-material.module";
import { ReactiveFormsModule } from "@angular/forms";

import { EditDialogComponent } from "./edit-dialog.component";
import { ColumnDefinition, CrudResource, MultiSelectColumn, SimpleColumn } from "../crudRepositoryApi";
import { MatButtonHarness } from "@angular/material/button/testing";
import { HarnessLoader } from "@angular/cdk/testing";
import { TestbedHarnessEnvironment } from "@angular/cdk/testing/testbed";
import { of } from "rxjs";
import { MatSelectHarness } from "@angular/material/select/testing";
import { MatOptionHarness } from "@angular/material/core/testing";

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

@NgModule({
  imports: [MatDialogModule, NoopAnimationsModule],
  exports: [EditDialogComponent],
  declarations: [EditDialogComponent],
  entryComponents: [
    EditDialogComponent
  ],
})
class EditDialogModule {
}

describe('EditDialogComponent', () => {
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

  let dialog: MatDialog;
  let overlayContainer: OverlayContainer;
  let component: EditDialogComponent<any>;
  let fixture: ComponentFixture<EditDialogComponent<any>>;
  let loader: HarnessLoader;
  let rootLoader: HarnessLoader;
  let mockDialogRef = {
    close: jasmine.createSpy('close')
  };

  let mockUserService: jasmine.SpyObj<RestService<User>> = jasmine.createSpyObj<RestService<User>>('RestService', {
    patch: of(null),
    create: of(null),
  });

  let mockRoleService: jasmine.SpyObj<RestService<Role>> = jasmine.createSpyObj<RestService<Role>>('RestService', {
    getAll: of([userRole, adminRole, otherRole]),
  });

  const columnDefinition: ColumnDefinition = {
    firstName: new SimpleColumn(
      'First name',
    ),
    lastName: new SimpleColumn(
      'Last name',
    ),
    roles: new MultiSelectColumn(
      'name',
      mockRoleService,
      'name',
    )
  }

  async function initTestBed(
    mockDialogRef: { close: jasmine.Spy },
    entity: User,
    columnDefinition: ColumnDefinition,
    mockUserService: RestService<User> & {
      resourceArray: RestService<User>["resourceArray"] extends Function ? (RestService<User>["resourceArray"] & jasmine.Spy) : RestService<User>["resourceArray"]; getAll: RestService<User>["getAll"] extends Function ? (RestService<User>["getAll"] & jasmine.Spy) : RestService<User>["getAll"]; get: RestService<User>["get"] extends Function ? (RestService<User>["get"] & jasmine.Spy) : RestService<User>["get"]; getBySelfLink: RestService<User>["getBySelfLink"] extends Function ? (RestService<User>["getBySelfLink"] & jasmine.Spy) : RestService<User>["getBySelfLink"]; search: RestService<User>["search"] extends Function ? (RestService<User>["search"] & jasmine.Spy) : RestService<User>["search"]; searchPage: RestService<User>["searchPage"] extends Function ? (RestService<User>["searchPage"] & jasmine.Spy) : RestService<User>["searchPage"]; searchSingle: RestService<User>["searchSingle"] extends Function ? (RestService<User>["searchSingle"] & jasmine.Spy) : RestService<User>["searchSingle"]; customQuery: RestService<User>["customQuery"] extends Function ? (RestService<User>["customQuery"] & jasmine.Spy) : RestService<User>["customQuery"]; customQueryPost: RestService<User>["customQueryPost"] extends Function ? (RestService<User>["customQueryPost"] & jasmine.Spy) : RestService<User>["customQueryPost"]; getByRelationArray: RestService<User>["getByRelationArray"] extends Function ? (RestService<User>["getByRelationArray"] & jasmine.Spy) : RestService<User>["getByRelationArray"]; getByRelation: RestService<User>["getByRelation"] extends Function ? (RestService<User>["getByRelation"] & jasmine.Spy) : RestService<User>["getByRelation"]; count: RestService<User>["count"] extends Function ? (RestService<User>["count"] & jasmine.Spy) : RestService<User>["count"]; create: RestService<User>["create"] extends Function ? (RestService<User>["create"] & jasmine.Spy) : RestService<User>["create"]; update: RestService<User>["update"] extends Function ? (RestService<User>["update"] & jasmine.Spy) : RestService<User>["update"]; patch: RestService<User>["patch"] extends Function ? (RestService<User>["patch"] & jasmine.Spy) : RestService<User>["patch"]; delete: RestService<User>["delete"] extends Function ? (RestService<User>["delete"] & jasmine.Spy) : RestService<User>["delete"]; totalElement: RestService<User>["totalElement"] extends Function ? (RestService<User>["totalElement"] & jasmine.Spy) : RestService<User>["totalElement"]; totalPages: RestService<User>["totalPages"] extends Function ? (RestService<User>["totalPages"] & jasmine.Spy) : RestService<User>["totalPages"]; hasFirst: RestService<User>["hasFirst"] extends Function ? (RestService<User>["hasFirst"] & jasmine.Spy) : RestService<User>["hasFirst"]; hasNext: RestService<User>["hasNext"] extends Function ? (RestService<User>["hasNext"] & jasmine.Spy) : RestService<User>["hasNext"]; hasPrev: RestService<User>["hasPrev"] extends Function ? (RestService<User>["hasPrev"] & jasmine.Spy) : RestService<User>["hasPrev"]; hasLast: RestService<User>["hasLast"] extends Function ? (RestService<User>["hasLast"] & jasmine.Spy) : RestService<User>["hasLast"]; next: RestService<User>["next"] extends Function ? (RestService<User>["next"] & jasmine.Spy) : RestService<User>["next"]; prev: RestService<User>["prev"] extends Function ? (RestService<User>["prev"] & jasmine.Spy) : RestService<User>["prev"]; first: RestService<User>["first"] extends Function ? (RestService<User>["first"] & jasmine.Spy) : RestService<User>["first"]; last: RestService<User>["last"] extends Function ? (RestService<User>["last"] & jasmine.Spy) : RestService<User>["last"]; page: RestService<User>["page"] extends Function ? (RestService<User>["page"] & jasmine.Spy) : RestService<User>["page"]
    },
    dialog: MatDialog,
    overlayContainer: OverlayContainer
  ) {
    await TestBed.configureTestingModule({
      imports: [
        NoopAnimationsModule,
        ReactiveFormsModule,
        AngularMaterialModule,
      ],
      providers: [
        { provide: MatDialogRef, useValue: mockDialogRef },
        {
          provide: MAT_DIALOG_DATA,
          useValue: {
            entity,
            modelType: User,
            formTemplate: columnDefinition,
            service: mockUserService,
          }
        }
      ],
      declarations: [EditDialogComponent],
    });
    TestBed.overrideModule(BrowserDynamicTestingModule, {
      set: {
        entryComponents: [EditDialogComponent]
      }
    });
    await TestBed.compileComponents();
    dialog = TestBed.inject(MatDialog);
    overlayContainer = TestBed.inject(OverlayContainer);
    fixture = TestBed.createComponent(EditDialogComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    rootLoader = TestbedHarnessEnvironment.documentRootLoader(fixture);
    fixture.detectChanges();
    return { dialog, overlayContainer };
  }

  describe('Update existing entity', () => {
    beforeEach(async () => {
      const entity = user;
      ({ dialog, overlayContainer } = await initTestBed(mockDialogRef, entity, columnDefinition, mockUserService, dialog, overlayContainer));
    });

    afterEach(() => {
      overlayContainer.ngOnDestroy();
    });

    it('should display the dialog with expected inputs', async () => {
      expect(component).toBeTruthy();
      const saveButton = await loader.getHarness(MatButtonHarness.with({ text: 'Submit' }));
      expect(saveButton).toBeTruthy();
      const cancelButton = await loader.getHarness(MatButtonHarness.with({ text: 'Cancel' }));
      expect(cancelButton).toBeTruthy();
      const roleSelect = await loader.getHarness(MatSelectHarness.with({ selector: 'mat-select' }));
      expect(roleSelect).toBeTruthy();

      expect(roleSelect.isMultiple()).toBeTruthy();
      await roleSelect.open();
      const options: MatOptionHarness[] = await roleSelect.getOptions();
      expect(options.length).toEqual(3);
      expect(await options[0].getText()).toEqual('USER');
      expect(await options[1].getText()).toEqual('ADMIN');
      expect(await options[0].isSelected()).toBeTrue();
      expect(await options[1].isSelected()).toBeTrue();
      expect(await options[2].isSelected()).not.toBeTrue();
    });

    it('should close the dialog', async () => {
      const cancelButton = await loader.getHarness(MatButtonHarness.with({ text: 'Cancel' }));
      await cancelButton.click();
      expect(mockDialogRef.close).toHaveBeenCalled();
    });

    it('should update entity', async () => {
      const roleSelect = await loader.getHarness(MatSelectHarness.with({ selector: 'mat-select' }));
      await roleSelect.open();
      await roleSelect.clickOptions({ text: 'OTHER'});

      const submitButton = await loader.getHarness(MatButtonHarness.with({ text: 'Submit' }));
      await submitButton.click();

      expect(mockUserService.patch.calls.mostRecent().args[0].roles).toEqual([userRole, adminRole, otherRole]);
      expect(mockDialogRef.close).toHaveBeenCalled();
    });
  });

  describe('Create new entity', () => {
    beforeEach(async () => {
      ({ dialog, overlayContainer } = await initTestBed(mockDialogRef, null, columnDefinition, mockUserService, dialog, overlayContainer));
    });

    it('should create entity', async () => {
      const roleSelect = await loader.getHarness(MatSelectHarness.with({ selector: 'mat-select' }));
      await roleSelect.open();
      await roleSelect.clickOptions({ text: 'OTHER'});

      const submitButton = await loader.getHarness(MatButtonHarness.with({ text: 'Submit' }));
      await submitButton.click();

      expect(mockUserService.create.calls.mostRecent().args[0].roles).toEqual([otherRole]);
      expect(mockDialogRef.close).toHaveBeenCalled();
    });

    afterEach(() => {
      overlayContainer.ngOnDestroy();
    });
  });
});
