import {Tenant} from "../tenants/tenant.model";
import {CrudEntity} from "../../shared/crudRepository/crudRepositoryApi";
import {Role} from "../roles/role.model";

export class User implements CrudEntity<User> {

  constructor(
    public id: number,
    public username: string,
    public firstName: string,
    public lastName: string,
    public email: string,
    public roles: Role[],
    public tenants: Tenant[],
  ) {
  }

  getId(): number {
    return this.id;
  }
}
