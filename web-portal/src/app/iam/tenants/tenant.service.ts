import { Injectable, Injector } from "@angular/core";
import { RestService } from "@lagoshny/ngx-hal-client";
import { User } from "../users/user.model";
import { Tenant } from "./tenant.model";

@Injectable({
  providedIn: 'root',
})
export class TenantService extends RestService<Tenant> {

  constructor(injector: Injector) {
    super(Tenant, 'tenants', injector);
  }
}

