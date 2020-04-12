import {Component} from '@angular/core';
import {TenantService} from "./tenant.service";
import {ColumnDefinition, ColumnType} from "../../shared/crudRepository/crudRepositoryApi";

@Component({
  selector: 'iam-tenants-table',
  template: `
    <crud-table-component
      [crudService]="service"
      [columnDefinition]="columnDefinition"
      [relation]="'tenants'"
    >
    </crud-table-component>`,
})
export class TenantsTableComponent {
  columnDefinition: ColumnDefinition = {
    schemaName: {
      type: ColumnType.STRING,
      label: 'Schema name',
    },
    createdAt: {
      type: ColumnType.STRING,
      label: 'Created at',
    },
    updatedAt: {
      type: ColumnType.STRING,
    },
  };

  constructor(public service: TenantService) {
  }
}
