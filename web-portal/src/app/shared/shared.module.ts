import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from "@angular/common";
import { AngularMaterialModule } from "./angular-material.module";
import { NgxHalClientModule } from '@lagoshny/ngx-hal-client';

import { CrudTableComponent } from "./crudRepository/crud-table.component";
import { EditDialogComponent } from "./crudRepository/editDialog/edit-dialog.component";
import { ExternalConfigurationService } from './ExternalConfigurationService'

@NgModule({
  declarations: [
    CrudTableComponent,
    EditDialogComponent,
  ],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    AngularMaterialModule,
    NgxHalClientModule.forRoot(),
    CommonModule,
  ],
  entryComponents: [
    EditDialogComponent
  ],
  providers: [
    { provide: 'ExternalConfigurationService', useClass: ExternalConfigurationService }
  ],
  exports: [
    CommonModule,
    AngularMaterialModule,
    CrudTableComponent,
  ]
})
export class SharedModule {
}
