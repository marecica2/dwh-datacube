import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface Page {
  size: number,
  totalElements: number,
  totalPages: number,
  number: number
}

export interface PaginationResponse<Entity> {
  _embedded: { users: Entity[] };
  page: Page;
}

export interface ColumnDefinition {
  [columnName: string]: {
    type: string,
    label?: string,
  }
}

export class CrudRepositoryService<Entity> {
  constructor(protected http: HttpClient, protected baseUrl: string) {
  }

  findAll(sort: string, order: string, page: number): Observable<PaginationResponse<Entity>> {
    const requestUrl = `${this.baseUrl}?sort=${sort},${order}&page=${page}&size=5`;
    return this.http.get<PaginationResponse<Entity>>(requestUrl);
  }

  create(entity: Entity): Observable<Entity> {
    const requestUrl = `${this.baseUrl}`;
    return this.http.post<Entity>(requestUrl, entity);
  }

  update(entity: Entity): Observable<Entity> {
    const requestUrl = `${this.baseUrl}`;
    return this.http.patch<Entity>(requestUrl, entity);
  }

  delete(entity: Entity): Observable<Entity> {
    const requestUrl = `${this.baseUrl}`;
    return this.http.delete<Entity>(requestUrl, entity);
  }
}
