import { Component } from '@angular/core';
import { TenantService } from "./tenant.service";
import { ColumnDefinition } from "../../shared/crudRepository/crudRepositoryApi";

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
      type: 'string',
      label: 'Schema name',
    },
    createdAt: {
      type: 'date',
      label: 'Created at',
    },
    updatedAt: {
      type: 'date',
    },
  };

  constructor(public service: TenantService) {
  }
}
