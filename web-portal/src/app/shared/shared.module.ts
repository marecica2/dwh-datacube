import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from "@angular/common";
import { AngularMaterialModule } from "./angular-material.module";
import { CrudTableComponent } from "./crudRepository/crud-table.component";
import { EditDialogComponent } from "./crudRepository/editDialog/edit-dialog.component";

@NgModule({
  declarations: [
    CrudTableComponent,
    EditDialogComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
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
