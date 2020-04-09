import { Component } from '@angular/core';
import { UserService } from "./user.service";
import { ColumnDefinition, ColumnType } from "../../shared/crudRepository/crudRepositoryApi";
import { Tenant } from "../tenants/tenant.model";
import { TenantService } from "../tenants/tenant.service";

@Component({
  selector: 'iam-users-table',
  template: `
    <crud-table-component
      [crudService]="service"
      [columnDefinition]="columnDefinition"
      [relation]="'users'"
      [editable]="true"
    >
    </crud-table-component>`,
})
export class UsersTableComponent {

  columnDefinition: ColumnDefinition = {
    username: {
      type: ColumnType.STRING,
      label: 'User name',
      formattedValue: (value: string) => value.toUpperCase(),
    },
    firstName: {
      type: ColumnType.STRING,
      label: 'First name',
    },
    lastName: {
      type: ColumnType.STRING,
      label: 'First name',
    },
    email: {
      type: ColumnType.STRING,
    },
    roles: {
      type: ColumnType.MULTI_SELECT,
      formattedValue: (roles: any[]) => {
        return roles.map(obj => obj.name).join(", ");
      },
      accessor: 'name',
    },
    tenants: {
      type: ColumnType.MULTI_SELECT,
      formattedValue: (tenants: Tenant[]) => tenants.map((tenant: Tenant) => tenant.schemaName).join(", "),
      accessor: 'schemaName',
    },
  };

  constructor(private service: UserService, private tenantService: TenantService) {
  }
}
