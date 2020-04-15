import { Injectable, Injector } from "@angular/core";
import { User } from "./user.model";
import { RestService } from "@lagoshny/ngx-hal-client";

@Injectable({
  providedIn: 'root',
})
export class UserService extends RestService<User> {

  constructor(injector: Injector) {
    super(User, 'users', injector);
  }
}
