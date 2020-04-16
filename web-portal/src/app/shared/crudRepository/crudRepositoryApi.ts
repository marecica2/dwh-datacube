import { Resource, RestService } from "@lagoshny/ngx-hal-client";

export abstract class CrudResource extends Resource {
  public abstract getIdentity(): any;
  public abstract getRelations(): string[];
  public abstract fromJson(json: any): CrudResource;
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
    public service: RestService<any>,
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
  [columnName: string]: SimpleColumn | SelectColumn;
}
