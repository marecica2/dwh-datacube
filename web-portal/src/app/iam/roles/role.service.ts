import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { CrudRepositoryService, PaginationResponse } from "../../shared/crudRepository/crudRepositoryApi";
import { from, Observable } from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class RoleService implements CrudRepositoryService<string> {

  constructor(http: HttpClient) {
  }

  findAll(): Observable<string[]> {
    return from([['USER', 'ADMIN']]);
  }

  create(entity: string): Observable<string> {
    return undefined;
  }

  delete(entity: string): Observable<string> {
    return undefined;
  }

  findAllPaginatedSorted(sort: string, order: string, page: number, pageSize: number): Observable<PaginationResponse<string>> {
    return undefined;
  }

  update(entity: string): Observable<string> {
    return undefined;
  }
}
