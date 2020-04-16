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
    this.id = json.id;
    this.username = json.username;
    this.firstName = json.firstName;
    this.lastName = json.lastName;
    this.roles =  json.roles.map(item => this._links.self.href + '/roles/' + item);
    this.tenants =  json.tenants.map(item => this._links.self.href + '/tenants/' + item);
    return this;
  }
}
