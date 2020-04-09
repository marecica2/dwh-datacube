import { Component, Inject } from "@angular/core";
import { MAT_DIALOG_DATA } from "@angular/material/dialog";
import { NgForm } from "@angular/forms";

import { ColumnDefinition, CrudRepositoryService } from "../crudRepositoryApi";

@Component({
  selector: 'edit-dialog',
  templateUrl: './edit-dialog.component.html',
  styleUrls: ['./edit-dialog.component.css']
})
export class EditDialogComponent<Entity> {
  private formTemplate: ColumnDefinition;
  private entity: Entity;
  private service: CrudRepositoryService<Entity>;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { entity: Entity, formTemplate: ColumnDefinition, service: CrudRepositoryService<Entity> }) {
    this.formTemplate = data.formTemplate;
    this.entity = data.entity;
    this.service = data.service;
  }

  formKeys() {
    return Object.keys(this.formTemplate);
  }

  onSubmit(form: NgForm) {
    console.log(form);
    if (!form.valid) {
      return;
    }
  }
}
