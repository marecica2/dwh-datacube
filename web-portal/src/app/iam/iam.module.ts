import { NgModule } from "@angular/core";
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from "../shared/shared.module";
import { CoreModule } from "../core.module";
import { UsersTableComponent } from './users/users-table.component';
import { TenantsTableComponent } from './tenants/tenants-table.component';
import { IamComponent } from './iam/iam.component';

const routes: Routes = [
  {
    path: "",
    redirectTo: "users"
  },
  {
    path: '', component: IamComponent, children: [
      { path: 'users', component: UsersTableComponent },
      { path: 'tenants', component: TenantsTableComponent },
      { path: 'apps', component: TenantsTableComponent },
      { path: 'tokens', component: TenantsTableComponent },
    ]
  },
];

@NgModule({
  declarations: [
    IamComponent,
    UsersTableComponent,
    TenantsTableComponent,
  ],
  imports: [
    RouterModule.forChild(routes),
    SharedModule,
    CoreModule,
  ]
})
export class IamModule {
}
