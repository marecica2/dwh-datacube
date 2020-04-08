import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { CrudRepositoryService } from "../../shared/crudRepository/crudRepositoryApi";
import { User } from "./user.model";

@Injectable({
  providedIn: 'root',
})
export class UserService extends CrudRepositoryService<User> {
  private static baseUrl = '/api/security/users';

  constructor(http: HttpClient) {
    super(http, UserService.baseUrl)
  }
}
