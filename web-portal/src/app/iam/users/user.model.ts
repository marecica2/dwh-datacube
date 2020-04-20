import { Tenant } from "../tenants/tenant.model";
import { Role } from "../roles/role.model";
import { CrudResource } from "../../shared/crudRepository/crudRepositoryApi";

export class User extends CrudResource {
  public id: number;
  public username: string;
  public firstName: string;
  public lastName: string;
  public email: string;
  public roles: Role[];
  public tenants: Tenant[];
  public createdOn: Date;
  public updatedOn: Date;

  getIdentity(): any {
    return this.id;
  }

  getIdentityKey(): string {
    return 'id';
  }

  getRelations(): string[] {
    return ['roles', 'tenant'];
  }
}
