import { Component } from '@angular/core';
import { ColumnDefinition, SelectColumn, SimpleColumn } from '../../shared/crudRepository/crudRepositoryApi';
import { TenantService } from "../tenants/tenant.service";
import { RoleService } from "../roles/role.service";
import { UserService } from "./user.service";

@Component({
  selector: 'iam-users-table',
  template: `
    <app-crud-table-component
      [crudService]="service"
      [columnDefinition]="columnDefinition"
      [relation]="'users'"
      [editable]="true"
    >
    </app-crud-table-component>`,
})
export class UsersTableComponent {
  columnDefinition: ColumnDefinition;

  constructor(public service: UserService, public tenantService: TenantService, public roleService: RoleService) {
    this.columnDefinition = {
      id: new SimpleColumn(
        'Id',
      ),
      username: new SimpleColumn(
        'User name',
        (value: string) => value.toUpperCase(),
      ),
      firstName: new SimpleColumn(
        'First name',
      ),
      lastName: new SimpleColumn(
        'Last name',
      ),
      email: new SimpleColumn(
      ),
      tenants: new SelectColumn(
        'schemaName',
        'schemaName',
        tenantService,
        'Tenants',
      ),
      roles: new SelectColumn(
        'name',
        'name',
        roleService,
        'Roles',
        (value: string) => value.toLowerCase(),
      ),
    };
  }
}
