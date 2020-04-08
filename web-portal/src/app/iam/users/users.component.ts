import { Component } from '@angular/core';
import { User, UserService } from "./user.service";
import { AbstractCrudRepositoryComponent } from "../../shared/crudRepository/crudRepositoryApi";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent extends AbstractCrudRepositoryComponent<User> {

  constructor(private userService: UserService) {
    super(userService);
  }

  columnDefinition(): string[] {
    return ['number', 'username', 'firstName', 'lastName', 'email'];
  }
}
