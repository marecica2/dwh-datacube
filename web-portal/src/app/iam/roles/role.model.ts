import { CrudResource } from "../../shared/crudRepository/crudRepositoryApi";

export class Role extends CrudResource {
  public id: number
  public name: string
  public description: string
  public createdOn: number
  public modifiedOn: number

  getIdentity(): any {
    return this.id;
  }

  getRelations(): string[] {
    return [];
  }

  fromJson(json: any): Role {
    this.id = json.id;
    this.name = json.name;
    this.description = json.description;
    this.createdOn = json.createdOn;
    this.modifiedOn = json.modifiedOn;
    return this;
  }
}
