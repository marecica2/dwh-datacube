import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface Page {
  size: number,
  totalElements: number,
  totalPages: number,
  number: number
}

export interface CrudRepositoryResponse<Entity> {
  _embedded: { users: Entity[] };
  page: Page;
}

export abstract class AbstractCrudRepositoryService<Entity> {
  constructor(protected http: HttpClient, protected baseUrl: string) {
  }

  findAll(sort: string, order: string, page: number): Observable<CrudRepositoryResponse<Entity>> {
    const requestUrl = `${this.baseUrl}?sort=${sort},${order}&page=${page}&size=5`;
    return this.http.get<CrudRepositoryResponse<Entity>>(requestUrl);
  }
}
