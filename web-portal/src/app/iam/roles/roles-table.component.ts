import { Component } from '@angular/core';
import { ColumnDefinition, SimpleColumn } from "../../shared/crudRepository/crudRepositoryApi";
import { RoleService } from "./role.service";

@Component({
  selector: 'app-roles-table',
  template: `
    <app-crud-table-component
      [crudService]="service"
      [columnDefinition]="columnDefinition"
      [relation]="'tenants'"
      [editable]="true"
    >
    </app-crud-table-component>`,
})
export class RolesTableComponent {
  columnDefinition: ColumnDefinition = {
    id: <SimpleColumn>{
      label: 'Id',
    },
    name: <SimpleColumn>{
      label: 'Name',
    },
    description: <SimpleColumn>{
      label: 'Description',
    },
    createdOn: <SimpleColumn>{
      label: 'Created on',
    },
    modifiedOn: <SimpleColumn>{
      label: 'Modified on',
    },
  };

  constructor(public service: RoleService) {
  }
}
