import { HttpClient } from "@angular/common/http";
import { merge, Observable, of as observableOf } from "rxjs";
import { AfterViewInit, ViewChild } from "@angular/core";
import { catchError, map, startWith, switchMap } from "rxjs/operators";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";

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

export abstract class AbstractCrudRepositoryComponent<Entity> implements AfterViewInit {
  data: Entity[] = [];
  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;
  pageSize = 5;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(protected service: AbstractCrudRepositoryService<Entity>) {
  }

  abstract columnDefinition(): string[];

  ngAfterViewInit() {
    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.service.findAll(this.sort.active, this.sort.direction, this.paginator.pageIndex);
        }),
        map((response: CrudRepositoryResponse<Entity>) => {
          this.isLoadingResults = false;
          this.resultsLength = response.page.totalElements;
          return response._embedded.users;
        }),
        catchError(() => {
          this.isLoadingResults = false;
          return observableOf([]);
        })
      ).subscribe((data: Entity[]) => this.data = data);
  }
}
