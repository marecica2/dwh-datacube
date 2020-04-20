import { Resource, RestService } from "@lagoshny/ngx-hal-client";


export interface CrudResourceIf<T> {
  new(...args: any[]): T;

  foo();
}

export abstract class CrudResource extends Resource {
  public abstract getIdentity(): any;

  public abstract getIdentityKey(): string;

  public abstract getRelations(): string[];
}

export class SimpleColumn {
  constructor(
    public columnLabel?: string,
    public formattedValue?: (value: any) => string,
    public readonly?: boolean,
  ) {
  }
}

export class SelectColumn {
  constructor(
    public displayValue: string | number,
    public service: RestService<any>,
    public columnLabel?: string,
    public formattedValue?: (value: any) => string,
    public readonly?: boolean,
  ) {
  }
}

export class MultiSelectColumn {
  constructor(
    public displayValue: any,
    public service: RestService<any>,
    public columnLabel?: string,
    public formattedValue?: (value: any) => string,
    public readonly?: boolean,
  ) {
  }
}

export enum ColumnType {
  SIMPLE = 'SimpleColumn',
  SELECT = 'SelectColumn',
  MULTI_SELECT = 'MultiSelectColumn',
}

export type Column = SimpleColumn | SelectColumn | MultiSelectColumn;

export interface ColumnDefinition {
  [columnName: string]: Column;
}
