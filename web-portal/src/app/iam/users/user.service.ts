import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { CrudRepositoryServiceImpl } from "../../shared/crudRepository/crudRepositoryApi";
import { User } from "./user.model";

@Injectable({
  providedIn: 'root',
})
export class UserService extends CrudRepositoryServiceImpl<User> {
  private static baseUrl = '/api/security/users/search/findAllEager';

  constructor(http: HttpClient) {
    super(http, UserService.baseUrl, 'users')
  }
}
