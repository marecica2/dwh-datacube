import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {
  CrudRepositoryService,
  CrudRepositoryServiceImpl,
  PaginationResponse
} from "../../shared/crudRepository/crudRepositoryApi";
import {from, Observable} from "rxjs";
import {Role} from "./role.model";
import {User} from "../users/user.model";

@Injectable({
  providedIn: 'root',
})
export class RoleService extends CrudRepositoryServiceImpl<User> {
  private static baseUrl = '/api/security';

  constructor(http: HttpClient) {
    super(http, RoleService.baseUrl, 'roles')
  }
}
