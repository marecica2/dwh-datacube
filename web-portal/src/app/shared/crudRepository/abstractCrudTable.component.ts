import { AfterViewInit, Component, ViewChild } from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { merge, of as observableOf } from "rxjs";
import { catchError, map, startWith, switchMap } from "rxjs/operators";
import { AbstractCrudRepositoryService, CrudRepositoryResponse } from "./crudRepositoryApi";

export abstract class AbstractCrudTableComponent<Entity> implements AfterViewInit {
  data: Entity[] = [];
  resultsLength = 0;
  isLoadingResults = true;
  pageSize = 5;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(protected service: AbstractCrudRepositoryService<Entity>) {
  }

  abstract columnDefinition(): string[];

  abstract relation(): string;

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
          return response._embedded[this.relation()];
        }),
        catchError(() => {
          this.isLoadingResults = false;
          return observableOf([]);
        })
      ).subscribe((data: Entity[]) => this.data = data);
  }
}
