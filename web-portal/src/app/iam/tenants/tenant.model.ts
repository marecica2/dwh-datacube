import { CrudResource } from "../../shared/crudRepository/crudRepositoryApi";

export class Tenant extends CrudResource {
  public id: string;
  public schemaName: string;
  public description: string;
  public createdAt: Date;
  public updatedAt: Date;

  getIdentity(): any {
    return this.id;
  }

  getRelations(): string[] {
    return [];
  }

  fromJson(json: any ): Tenant {
    this.id = json.id;
    this.schemaName = json.schemaName;
    this.description = json.description;
    this.createdAt = json.createdAt;
    this.updatedAt = json.updatedAt;
    return this;
  }
}
