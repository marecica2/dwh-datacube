import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AngularMaterialModule } from "./angular-material.module";

@NgModule({
  declarations: [],
  imports: [
    AngularMaterialModule,
    CommonModule,
  ],
  exports: [
    AngularMaterialModule,
    CommonModule,
  ]
})
export class SharedModule {
}
