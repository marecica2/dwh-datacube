import { Component } from '@angular/core';
import { UserService } from "./user.service";
import { ColumnDefinition } from "../../shared/crudRepository/crudRepositoryApi";

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
      type: 'string',
      label: 'User name',
    },
    firstName: {
      type: 'string',
      label: 'First name',
    },
    lastName: {
      type: 'string',
      label: 'First name',
    },
    email: {
      type: 'string',
    },
  };

  constructor(public service: UserService) {
  }
}
