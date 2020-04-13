import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

export interface Page {
  size: number;
  totalElements: number;
  totalPages: number;
  number: number;
}

export interface PaginationResponse<Entity> {
  _embedded: { users: Entity[] };
  page: Page;
}

export class SimpleColumn {
  constructor(
    public columnLabel?: string,
    public formattedValue?: (value: any) => string,
  ) {
  }
}

export class SelectColumn {
  constructor(
    public value: string | number,
    public displayValue: string | number,
    public service: CrudRepositoryService<any>,
    public columnLabel?: string,
    public formattedValue?: (value: any) => string,
  ) {
  }
}

export enum ColumnType {
  SIMPLE = 'SimpleColumn',
  SELECT = 'SelectColumn',
}

export type Column = SimpleColumn | SelectColumn;

export interface ColumnDefinition {
  [columnName: string]: Column;
}

export interface CrudRepositoryService<Entity> {
  findAll(): Observable<object[]>;

  findAllPaginatedSorted(sort: string, order: string, page: number, pageSize: number): Observable<PaginationResponse<Entity>>;

  create(entity: Entity): Observable<Entity>;

  update(entity: Entity): Observable<Entity>;

  delete(entity: Entity): Observable<Entity>;
}

export class CrudRepositoryServiceImpl<Entity> implements CrudRepositoryService<Entity> {
  constructor(protected http: HttpClient, protected baseUrl: string, protected relation: string) {
  }

  findAll(): Observable<Entity[]> {
    const requestUrl = `${this.baseUrl}`;
    return this.http.get<Entity[]>(requestUrl).pipe(map(resp => resp['_embedded'][this.relation] as Entity[]));
  }

  findAllPaginatedSorted(sort: string, order: string, page: number, pageSize: number): Observable<PaginationResponse<Entity>> {
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
