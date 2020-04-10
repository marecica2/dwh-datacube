import { AfterViewInit, Component, Input, OnInit, ViewChild } from "@angular/core";
import { merge, of as observableOf } from "rxjs";
import { catchError, map, startWith, switchMap } from "rxjs/operators";

import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatDialog } from "@angular/material/dialog";

import { ColumnDefinition, ColumnType, CrudRepositoryServiceImpl, PaginationResponse } from "./crudRepositoryApi";
import { EditDialogComponent } from "./editDialog/edit-dialog.component";

@Component({
  selector: 'crud-table-component',
  templateUrl: './crud-table.component.html',
  styleUrls: ['./crud-table.component.css'],
  exportAs: 'abstractCrudTableComponent',
})
export class CrudTableComponent<Entity> implements AfterViewInit {
  public columnType = ColumnType;
  private data: Entity[] = [];
  private page = 0;

  private isLoadingResults = true;

  @Input('columnDefinition') columnDefinition: ColumnDefinition = {};
  @Input('crudService') service: CrudRepositoryServiceImpl<Entity>;
  @Input('relation') relation: string;
  @Input('editable') editable: boolean;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private dialog: MatDialog) {
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.service.findAllPaginatedSorted(this.sort.active, this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
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

  private openEditDialog(entity: Entity) {
    this.dialog.open(EditDialogComponent, {
      data: { entity, formTemplate: this.columnDefinition, service: this.service },
    });
  }

  private getColumnKeys(): string[] {
    return Object.keys(this.columnDefinition);
  }

  private getHeaderDefKeys(): string[] {
    if (this.editable) {
      return Object.keys(this.columnDefinition).concat(['actions']);
    }
    return Object.keys(this.columnDefinition);
  }
}
