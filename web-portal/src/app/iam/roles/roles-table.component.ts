import { Component } from '@angular/core';
import { ColumnDefinition, SimpleColumn } from "../../shared/crudRepository/crudRepositoryApi";
import { RoleService } from "./role.service";
import { Role } from "./role.model";
import * as moment from "moment";

@Component({
  selector: 'app-roles-table',
  template: `
    <app-crud-table-component
      [crudService]="service"
      [columnDefinition]="columnDefinition"
      [relation]="'tenants'"
      [editable]="true"
      [modelType]="roleRef"
    >
    </app-crud-table-component>`,
})
export class RolesTableComponent {
  roleRef = Role;

  columnDefinition: ColumnDefinition = {
    name: new SimpleColumn(
      'Name'
    ),
    description: new SimpleColumn(
      'Description'
    ),
    createdOn: new SimpleColumn(
      'Created',
      (value: string) => moment(value).fromNow().toString(),
    ),
    modifiedOn: new SimpleColumn(
      'Updated',
      (value: string) => moment(value).fromNow().toString(),
    ),
  };

  constructor(public service: RoleService) {
  }
}
