import { Component } from '@angular/core';
import { UserService } from "./user.service";
import { ColumnDefinition, ColumnType } from "../../shared/crudRepository/crudRepositoryApi";

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
  };

  constructor(public service: UserService) {
  }
}
