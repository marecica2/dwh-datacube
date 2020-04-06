import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppsComponent } from "./components/pages/apps/apps.component";

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'apps' },
  {
    path: 'auth', loadChildren: () =>
      import("./auth/auth.module").then(m => m.AuthModule)
  },
  { path: 'apps', component: AppsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule {
}
