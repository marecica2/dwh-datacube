import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppsComponent } from "./components/pages/apps/apps.component";
import { RoleGuard } from "./auth/role.guard";

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'apps' },
  {
    path: 'auth', loadChildren: () =>
      import("./auth/auth.module").then(m => m.AuthModule)
  },
  {
    path: 'iam', loadChildren: () =>
      import("./iam/iam.module").then(m => m.IamModule)
  },
  {
    path: 'apps',
    component: AppsComponent,
    canActivate: [RoleGuard], data: {roles: ['USER']},
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule {
}
