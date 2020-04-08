import { Component } from '@angular/core';
import { UserService } from "./user.service";
import { AbstractCrudTableComponent } from "../../shared/crudRepository/abstractCrudTable.component";
import { User } from "./user.model";

@Component({
  selector: 'iam-users-table',
  templateUrl: '../../shared/crudRepository/abstractCrudTable.component.html',
  styleUrls: ['../../shared/crudRepository/abstractCrudTable.component.css']
})
export class UsersTableComponent extends AbstractCrudTableComponent<User> {

  constructor(private userService: UserService) {
    super(userService);
  }

  relation(): string {
    return "users";
  }

  columnDefinition(): string[] {
    return ['username', 'firstName', 'lastName', 'email'];
  }
}
