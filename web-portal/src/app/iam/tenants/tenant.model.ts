import {CrudEntity} from "../../shared/crudRepository/crudRepositoryApi";

export class Tenant implements CrudEntity<Tenant> {
  constructor(public id: string, public schemaName: string) {
  }

  getId(): any {
    return this.id;
  }

  fromJson(data: object): Tenant {
    return undefined;
  }
}
