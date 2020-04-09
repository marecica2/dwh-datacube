import { Tenant } from "../tenants/tenant.model";

export interface User {
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  roles: string[];
  tenants: Tenant[];
}
