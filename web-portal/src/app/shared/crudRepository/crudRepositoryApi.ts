import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface Page {
  size: number,
  totalElements: number,
  totalPages: number,
  number: number
}

export enum ColumnType {
  STRING = 'string',
  NUMBER = 'number',
  SELECT = 'select',
  MULTI_SELECT = 'multiSelect',
}

export interface PaginationResponse<Entity> {
  _embedded: { users: Entity[] };
  page: Page;
}

export interface ColumnDefinition {
  [columnName: string]: {
    type: ColumnType,
    label?: string,
    formattedValue?: (value: any) => string;
    selectValuesProvider?: (value: any) => string;
    accessor?: string,
  }
}

export interface ColumnDef {
  label?: string,
  formattedValue?: (value: any) => string;
}

export interface MultiValueColumnDef extends ColumnDef{
  selectValuesProvider: (value: any) => string;
}

export class CrudRepositoryService<Entity> {
  constructor(protected http: HttpClient, protected baseUrl: string) {
  }

  findAll(sort: string, order: string, page: number, pageSize: number): Observable<PaginationResponse<Entity>> {
    const requestUrl = `${this.baseUrl}?sort=${sort},${order}&page=${page}&size=${pageSize}`;
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
