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
  private data: Entity[] = [];
  private page = 0;
  private pageSize = 5;

  private isLoadingResults = true;

  @Input('columnDefinition') columnDefinition: ColumnDefinition = {};
  @Input('crudService') service: CrudRepositoryService<Entity>;
  @Input('relation') relation: string;
  @Input('editable') editable: boolean;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  ngOnInit(): void {
  }

  getColumnKeys(): string[] {
    return Object.keys(this.columnDefinition);
  }

  getHeaderDefKeys(): string[] {
    if (this.editable) {
      return Object.keys(this.columnDefinition).concat(['actions']);
    }
    return Object.keys(this.columnDefinition);
  }

  getEntity(el: Element) {
    console.log(el);
  }

  ngAfterViewInit() {

    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.service.findAll(this.sort.active, this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
        }),
        map((response: PaginationResponse<Entity>) => {
          this.isLoadingResults = false;
          this.page = response.page.totalElements;
          return response._embedded[this.relation];
        }),
        catchError(() => {
          this.isLoadingResults = false;
          return observableOf([]);
        })
      ).subscribe((data: Entity[]) => this.data = data);
  }
}
