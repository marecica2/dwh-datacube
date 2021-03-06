import { AfterViewInit, Component, Input, ViewChild } from "@angular/core";
import { merge, of as observableOf, Subject } from "rxjs";
import { catchError, delay, map, startWith, switchMap } from "rxjs/operators";
import { RestService } from "@lagoshny/ngx-hal-client";

import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatDialog } from "@angular/material/dialog";

import { ColumnDefinition, ColumnType, CrudResource, MultiSelectColumn, SimpleColumn } from "./crudRepositoryApi";
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
  @Input('modelType') modelType: { new(...args: any[]): Entity; };

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
    const column = this.columnDefinition[key] as SimpleColumn;
    const value = row[key];
    return this.getFormattedValue(value, column);
  }

  getSelectValue(row: Entity, key: string, item: object) {
    const column = this.columnDefinition[key] as MultiSelectColumn;
    const value = item[column.displayValue];
    return this.getFormattedValue(value, column);
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page, this.reloadSubject)
      .pipe(
        startWith({}),
        delay(0), // Fix from https://blog.angular-university.io/angular-debugging/
        switchMap(() => {
          this.isLoadingResults = true;
          const query = [{ key: 'sort', value: `${this.sort.active},${this.sort.direction}` }, {
            key: 'page',
            value: this.paginator.pageIndex
          }];
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
      ).subscribe((response: Entity[]) => this.data = response);
  }

  editEntity(entity: Entity) {
    this.service.get(entity.getIdentity(), [{ key: 'projection', value: 'full' }]).subscribe(editableEntity => {
      this.openDialog(editableEntity);
    })
  }

  public openDialog(entity: Entity) {
    const dialogRef = this.dialog.open(EditDialogComponent, {
      data: { entity, modelType: this.modelType, formTemplate: this.columnDefinition, service: this.service },
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result && result.success) {
        this.reloadSubject.next();
      }
    });
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

  private getFormattedValue(value: any, col: SimpleColumn | MultiSelectColumn) {
    if (col.formattedValue != null) {
      return col.formattedValue(value);
    }
    return value;
  }
}
