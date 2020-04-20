import { Component } from '@angular/core';
import {
  ColumnDefinition,
  MultiSelectColumn,
  SelectColumn,
  SimpleColumn
} from '../../shared/crudRepository/crudRepositoryApi';
import * as moment from 'moment';

import { TenantService } from "../tenants/tenant.service";
import { RoleService } from "../roles/role.service";
import { UserService } from "./user.service";
import { User } from "./user.model";

@Component({
  selector: 'iam-users-table',
  template: `
    <app-crud-table-component
      [crudService]="service"
      [columnDefinition]="columnDefinition"
      [relation]="'users'"
      [editable]="true"
      [modelType]="userRef"
    >
    </app-crud-table-component>`,
})
export class UsersTableComponent {
  columnDefinition: ColumnDefinition;
  userRef = User;

  constructor(public service: UserService, public tenantService: TenantService, public roleService: RoleService) {
    this.columnDefinition = {
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
      tenants: new MultiSelectColumn(
        'name',
        tenantService,
        'Tenants',
        null,
      ),
      roles: new MultiSelectColumn(
        'name',
        roleService,
        'Roles',
        (value: string) => value.toLowerCase(),
      ),
      createdOn: new SimpleColumn(
        'Created',
        (value: string) => moment(value).fromNow().toString(),
        true,
      ),
      modifiedOn: new SimpleColumn(
        'Modified',
        (value: string) => moment(value).fromNow().toString(),
        true,
      ),
    };
  }
}
