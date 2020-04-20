import { Component } from '@angular/core';
import { TenantService } from "./tenant.service";
import { ColumnDefinition, SimpleColumn } from "../../shared/crudRepository/crudRepositoryApi";
import { Tenant } from "./tenant.model";
import * as moment from "moment";

@Component({
  selector: 'app-tenants-table',
  template: `
    <app-crud-table-component
      [crudService]="service"
      [columnDefinition]="columnDefinition"
      [relation]="'tenants'"
      [editable]="true"
      [modelType]="tenantRef"
    >
    </app-crud-table-component>`,
})
export class TenantsTableComponent {
  tenantRef = Tenant;

  columnDefinition: ColumnDefinition = {
    id: new SimpleColumn(
      'Tenant Id'
    ),
    name: new SimpleColumn(
        'Tenant Name'
    ),
    description: new SimpleColumn(
        'Description'
    ),
    createdOn: new SimpleColumn(
        'Created',
        (value: string) => moment(value).fromNow().toString(),
    ),
    modifiedOn: new SimpleColumn(
        'Modified',
        (value: string) => moment(value).fromNow().toString(),
    ),
  };

  constructor(public service: TenantService) {
  }
}
