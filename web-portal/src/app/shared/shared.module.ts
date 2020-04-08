import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AngularMaterialModule } from "./angular-material.module";
import { CrudTableComponent } from "./crudRepository/crud-table.component";

@NgModule({
  declarations: [
    CrudTableComponent,
  ],
  imports: [
    AngularMaterialModule,
    CommonModule,
  ],
  exports: [
    CommonModule,
    AngularMaterialModule,
    CrudTableComponent,
  ]
})
export class SharedModule {
}
