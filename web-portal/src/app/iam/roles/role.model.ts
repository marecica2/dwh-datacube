import { CrudResource } from "../../shared/crudRepository/crudRepositoryApi";

export class Role extends CrudResource {
  public id: number;
  public name: string;
  public description: string
  public createdOn: Date;
  public updatedOn: Date;

  getIdentity(): any {
    return this.id;
  }

  getIdentityKey(): string {
    return 'id';
  }

  getRelations(): string[] {
    return [];
  }
}
