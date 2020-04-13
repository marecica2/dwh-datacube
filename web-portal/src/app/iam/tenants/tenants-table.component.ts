import {Component} from '@angular/core';
import {TenantService} from "./tenant.service";
import {SimpleColumn, ColumnDefinition, ColumnType} from "../../shared/crudRepository/crudRepositoryApi";

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
    schemaName: <SimpleColumn>{
      label: 'Schema name',
    },
    createdAt: <SimpleColumn>{
      label: 'Created at',
    },
    updatedAt: <SimpleColumn>{
    },
  };

  constructor(public service: TenantService) {
  }
}
