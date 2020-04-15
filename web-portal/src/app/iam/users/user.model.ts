import { Tenant } from "../tenants/tenant.model";
import { Role } from "../roles/role.model";
import { CrudResource } from "../../shared/crudRepository/crudRepositoryApi";

export class User extends CrudResource {
  public id: number
  public username: string
  public firstName: string
  public lastName: string
  public email: string
  public roles: Role[]
  public tenants: Tenant[]

  getIdentity(): any {
    return this.id;
  }

  getRelations(): string[] {
    return ['roles', 'tenant'];
  }

  fromJson(json: any ): User {
    const r: Role = new Role();
    const t: Tenant = new Tenant();
    this.id = json.id;
    this.username = json.username;
    this.firstName = json.firstName;
    this.lastName = json.firstName;
    this.roles = this.roles.map((item, index) => item.fromJson(json.roles[index]))
    this.tenants = this.tenants.map((item, index) => item.fromJson(json.roles[index]))
    return this;
  }
}
