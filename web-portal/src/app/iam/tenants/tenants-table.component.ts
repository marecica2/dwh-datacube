import {Component} from '@angular/core';
import {TenantService} from "./tenant.service";
import {SimpleColumn, ColumnDefinition, ColumnType} from "../../shared/crudRepository/crudRepositoryApi";

@Component({
  selector: 'app-tenants-table',
  template: `
    <app-crud-table-component
      [crudService]="service"
      [columnDefinition]="columnDefinition"
      [relation]="'tenants'"
      [editable]="true"
    >
    </app-crud-table-component>`,
})
export class TenantsTableComponent {
  columnDefinition: ColumnDefinition = {
    id: <SimpleColumn>{
      label: 'Tenant Id',
    },
    description: <SimpleColumn>{
      label: 'Description',
    },
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
