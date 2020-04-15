import { Injectable, Injector } from "@angular/core";
import { Role } from "./role.model";
import { RestService } from "@lagoshny/ngx-hal-client";

@Injectable({
  providedIn: 'root',
})
export class RoleService extends RestService<Role> {

  constructor(injector: Injector) {
    super(Role, 'roles', injector);
  }
}

