import { NgModule } from "@angular/core";
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from "../shared/shared.module";
import { CoreModule } from "../core.module";
import { UsersTableComponent } from './users/users-table.component';
import { TenantsTableComponent } from './tenants/tenants-table.component';
import { IamComponent } from './iam/iam.component';
import { RolesTableComponent } from "./roles/roles-table.component";
import { RoleGuard } from "../auth/role.guard";

const routes: Routes = [
  {
    path: "",
    redirectTo: "users"
  },
  {
    path: '', component: IamComponent, children: [
      { path: 'users', component: UsersTableComponent },
      { path: 'tenants', component: TenantsTableComponent },
      { path: 'roles', component: RolesTableComponent },
      { path: 'apps', component: TenantsTableComponent },
      { path: 'tokens', component: TenantsTableComponent },
    ],
    canActivate: [RoleGuard], data: {roles: ['ADMIN']},
  },
];

@NgModule({
  declarations: [
    IamComponent,
    UsersTableComponent,
    TenantsTableComponent,
    RolesTableComponent,
  ],
  imports: [
    RouterModule.forChild(routes),
    SharedModule,
    CoreModule,
  ]
})
export class IamModule {
}
