import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';

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

export interface EntityResponse<Entity> {
  [key: string]: any,

  _links: {
    [key: string]: { href: string },
    self: { href: string },
  }
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

export interface CrudEntity<Entity> {
  getId(): any;
}

export abstract class BaseCrudEntity<Entity> implements CrudEntity<Entity> {
  abstract getId(): any;
}

export interface CrudRepositoryService<Entity> {
  getById(id: any): Observable<Entity>;

  findAll(): Observable<Entity[]>;

  findAllPaginatedSorted(
    sort: string, order: string, page: number, pageSize: number): Observable<PaginationResponse<Entity>>;

  create(entity: Entity): Observable<Entity>;

  update(id: any, entity: Entity): Observable<Entity>;

  delete(id: any, entity: Entity): Observable<Entity>;

  fromJson(data: object): Entity;
}

export abstract class CrudRepositoryServiceImpl<Entity> implements CrudRepositoryService<Entity> {
  constructor(protected http: HttpClient, protected baseUrl: string, protected relation: string) {
  }

  getById(id: any): Observable<Entity> {
    const requestUrl = `${this.baseUrl}/${this.relation}/${id}?projection=full`;
    return this.http.get<Entity>(requestUrl).pipe(
      tap(data => console.log(data)),
      map((resp) => {
        return resp;
      }),
    );
  }

  findAll(): Observable<Entity[]> {
    const requestUrl = `${this.baseUrl}/${this.relation}`;
    return this.http.get<Entity[]>(requestUrl).pipe(map(resp => resp['_embedded'][this.relation] as Entity[]));
  }

  findAllPaginatedSorted(
    sort: string, order: string, page: number, pageSize: number): Observable<PaginationResponse<Entity>> {
    const requestUrl = `${this.baseUrl}/${this.relation}?sort=${sort},${order}&page=${page}&size=${pageSize}`;
    return this.http.get<PaginationResponse<Entity>>(requestUrl);
  }

  create(entity: Entity): Observable<Entity> {
    const requestUrl = `${this.baseUrl}/${this.relation}`;
    return this.http.post<Entity>(requestUrl, entity);
  }

  update(id: any, entity: Entity): Observable<Entity> {
    console.log(entity);
    const requestUrl = `${this.baseUrl}/${this.relation}/${id}`;
    return this.http.put<Entity>(requestUrl, entity);
  }

  delete(id: any, entity: Entity): Observable<Entity> {
    const requestUrl = `${this.baseUrl}/${this.relation}/${id}`;
    return this.http.delete<Entity>(requestUrl, entity);
  }

  abstract fromJson(data: object): Entity;
}
