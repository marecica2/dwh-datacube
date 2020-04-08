import { AfterViewInit, Component, Input, OnInit, ViewChild } from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { merge, of as observableOf } from "rxjs";
import { catchError, map, startWith, switchMap } from "rxjs/operators";
import { ColumnDefinition, CrudRepositoryService, PaginationResponse } from "./crudRepositoryApi";

@Component({
  selector: 'crud-table-component',
  templateUrl: './crud-table.component.html',
  styleUrls: ['./crud-table.component.css'],
  exportAs: 'abstractCrudTableComponent',
})
export class CrudTableComponent<Entity> implements AfterViewInit, OnInit {
  data: Entity[] = [];
  columnKeys: string[];
  resultsLength = 0;
  isLoadingResults = true;
  pageSize = 5;

  @Input('columnDefinition') columnDefinition: ColumnDefinition;
  @Input('relation') relation: string;
  @Input('crudService') service: CrudRepositoryService<Entity>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  ngOnInit(): void {
    this.columnKeys = Object.keys(this.columnDefinition);
  }

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
        map((response: PaginationResponse<Entity>) => {
          this.isLoadingResults = false;
          this.resultsLength = response.page.totalElements;
          return response._embedded[this.relation];
        }),
        catchError(() => {
          this.isLoadingResults = false;
          return observableOf([]);
        })
      ).subscribe((data: Entity[]) => this.data = data);
  }
}
