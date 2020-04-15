import { AfterViewInit, Component, Input, ViewChild } from "@angular/core";
import { merge, of as observableOf, Subject } from "rxjs";
import { catchError, map, startWith, switchMap, tap } from "rxjs/operators";
import { Resource, RestService } from "@lagoshny/ngx-hal-client";

import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatDialog } from "@angular/material/dialog";

import { Column, ColumnDefinition, ColumnType, CrudResource, SelectColumn, SimpleColumn } from "./crudRepositoryApi";
import { EditDialogComponent } from "./editDialog/edit-dialog.component";


@Component({
  selector: 'app-crud-table-component',
  templateUrl: './crud-table.component.html',
  styleUrls: ['./crud-table.component.css'],
  exportAs: 'abstractCrudTableComponent',
})
export class CrudTableComponent<Entity extends CrudResource> implements AfterViewInit {
  @Input('columnDefinition') columnDefinition: ColumnDefinition = {};
  @Input('crudService') service: RestService<Entity>;
  @Input('relation') relation: string;
  @Input('editable') editable: boolean;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  public columnType = ColumnType;
  public data: Entity[] = [];
  public length = 0;
  public isLoadingResults = true;
  public reloadSubject = new Subject<void>();

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

    merge(this.sort.sortChange, this.paginator.page, this.reloadSubject)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          const query = [{ key: 'sort', value: `${this.sort.active},${this.sort.direction}` }, { key: 'page', value: this.paginator.pageIndex }];
          return this.service.getAll(
            { size: this.paginator.pageSize, params: query },
          );
        }),
        map((response: any) => {
          this.length = this.service.totalElement();
          this.isLoadingResults = false;
          return response;
        }),
        catchError(() => {
          this.isLoadingResults = false;
          return observableOf([]);
        })
      ).subscribe((data: Entity[]) => this.data = data);
  }

  openEditDialog(entity: Entity) {
    this.service.get(entity.getIdentity(), [{key: 'projection', value: 'full'}] ).subscribe(data => {
      const  dialogRef = this.dialog.open(EditDialogComponent, {
        data: { entity: data, formTemplate: this.columnDefinition, service: this.service },
      });
      dialogRef.afterClosed().subscribe(result => {
        if(result && result.success) {
          this.reloadSubject.next();
        }
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
