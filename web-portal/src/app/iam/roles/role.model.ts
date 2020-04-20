import { CrudResource } from "../../shared/crudRepository/crudRepositoryApi";

export class Role extends CrudResource {
  public id: number;
  public name: string;
  public description: string
  public createdAt: Date;
  public updatedAt: Date;

  getIdentity(): any {
    return this.id;
  }

  getIdentityKey(): string {
    return 'id';
  }

  getRelations(): string[] {
    return [];
  }

  fromJson(json: any): Role {
    this.id = json.id;
    this.name = json.name;
    this.description = json.description;
    this.createdAt = json.createdAt;
    this.updatedAt = json.updatedAt;
    return this;
  }

  toString() {

  }
}
