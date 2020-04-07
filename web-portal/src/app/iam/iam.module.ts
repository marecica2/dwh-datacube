import { NgModule } from "@angular/core";
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from "../shared/shared.module";
import { CoreModule } from "../core.module";
import { UsersComponent } from './users/users.component';
import { TenantsComponent } from './tenants/tenants.component';
import { IamComponent } from './iam/iam.component';

const routes: Routes = [
  {
    path: '', component: IamComponent, children: [
      { path: 'users', component: UsersComponent },
      { path: 'tenants', component: TenantsComponent },
      { path: 'apps', component: TenantsComponent },
      { path: 'tokens', component: TenantsComponent },
    ]
  },
];

@NgModule({
  declarations: [
    IamComponent,
    UsersComponent,
    TenantsComponent,
  ],
  imports: [
    RouterModule.forChild(routes),
    SharedModule,
    CoreModule,
  ]
})
export class IamModule {
}
