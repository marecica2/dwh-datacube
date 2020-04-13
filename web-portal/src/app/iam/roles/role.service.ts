import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {CrudRepositoryService, PaginationResponse} from "../../shared/crudRepository/crudRepositoryApi";
import {from, Observable} from "rxjs";
import {Role} from "./role.model";

@Injectable({
  providedIn: 'root',
})
export class RoleService implements CrudRepositoryService<Role> {

  constructor(http: HttpClient) {
  }

  findAll(): Observable<Role[]> {
    return from([[{name: 'USER'}, {name: 'ADMIN'}]]);
  }

  create(entity: Role): Observable<Role> {
    return undefined;
  }

  delete(entity: Role): Observable<Role> {
    return undefined;
  }

  findAllPaginatedSorted(sort: string, order: string, page: number, pageSize: number): Observable<PaginationResponse<Role>> {
    return undefined;
  }

  update(entity: Role): Observable<Role> {
    return undefined;
  }
}
