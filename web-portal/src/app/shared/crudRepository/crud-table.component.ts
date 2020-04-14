import { AfterViewInit, Component, Input, ViewChild } from "@angular/core";
import { merge, of as observableOf } from "rxjs";
import { catchError, map, startWith, switchMap } from "rxjs/operators";

import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatDialog } from "@angular/material/dialog";

import {
  Column,
  ColumnDefinition,
  ColumnType, CrudEntity,
  CrudRepositoryServiceImpl,
  PaginationResponse,
  SelectColumn,
  SimpleColumn
} from "./crudRepositoryApi";
import { EditDialogComponent } from "./editDialog/edit-dialog.component";

@Component({
             selector: 'app-crud-table-component',
             templateUrl: './crud-table.component.html',
             styleUrls: ['./crud-table.component.css'],
             exportAs: 'abstractCrudTableComponent',
           })
export class CrudTableComponent<Entity extends CrudEntity<Entity>> implements AfterViewInit {
  public columnType = ColumnType;
  data: Entity[] = [];
  page = 0;

  isLoadingResults = true;

  @Input('columnDefinition') columnDefinition: ColumnDefinition = {};
  @Input('crudService') service: CrudRepositoryServiceImpl<CrudEntity<Entity>>;
  @Input('relation') relation: string;
  @Input('editable') editable: boolean;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private dialog: MatDialog) {
  }

  getType(object: Object): string {
    return object.constructor.name;
  }

  getSimpleValue(row: Entity, key: string) {
    const column: Column = this.columnDefinition[key];
    const value = row[key];
    return this.getFormattedValue(value, column);
  }

  getSelectValue(row: Entity, key: string, item: object) {
    const column = this.columnDefinition[key] as SelectColumn;
    const value = item[column.displayValue];
    return this.getFormattedValue(value, column);
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.service.findAllPaginatedSorted(
            this.sort.active,
            this.sort.direction,
            this.paginator.pageIndex,
            this.paginator.pageSize
          );
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

  openEditDialog(data: object) {
    const entity = this.service.fromJson(data);
    this.service.getById(entity.getId()).subscribe(data => {
      console.log(data);
      this.dialog.open(EditDialogComponent, {
        data: {entity, formTemplate: this.columnDefinition, service: this.service},
      });
    })
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

  private getFormattedValue(value: any, col: SimpleColumn | SelectColumn) {
    if (col.formattedValue != null) {
      return col.formattedValue(value);
    }
    return value;
  }
}
