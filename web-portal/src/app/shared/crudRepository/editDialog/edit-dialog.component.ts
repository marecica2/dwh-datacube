import { Component, Inject, OnInit } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormControl, FormGroup } from "@angular/forms";

import { ColumnDefinition, ColumnType, CrudResource, SelectColumn } from "../crudRepositoryApi";
import { Resource, RestService } from "@lagoshny/ngx-hal-client";

@Component({
  selector: 'edit-dialog',
  templateUrl: './edit-dialog.component.html',
  styleUrls: ['./edit-dialog.component.css']
})
export class EditDialogComponent<Entity extends CrudResource> implements OnInit {
  public json = JSON;
  public columnType = ColumnType;
  public formTemplate: ColumnDefinition;
  public entity: Entity;
  public selectValues = {};
  public editForm: FormGroup;
  private service: RestService<Entity>;

  constructor(
    public dialogRef: MatDialogRef<EditDialogComponent<Entity>>,
    @Inject(MAT_DIALOG_DATA) public data: {
      success: boolean;
      entity: Entity,
      formTemplate: ColumnDefinition,
      service: RestService<Entity>
    }
  ) {
    this.formTemplate = data.formTemplate;
    this.entity = data.entity;
    this.editForm = new FormGroup({});
    this.service = data.service;
  }

  getType(object: Object): string {
    return object.constructor.name;
  }

  ngOnInit(): void {
    Object.keys(this.formTemplate).forEach((formItem: string) => {
      switch (this.getType(this.formTemplate[formItem])) {
        case ColumnType.SELECT:
          const selectFormElement = this.formTemplate[formItem] as SelectColumn;
          selectFormElement.service
            .getAll()
            .subscribe(data => {
              const attribute = selectFormElement.value;
              const values = this.entity[formItem].map(val => val[attribute]);
              this.editForm.addControl(formItem, new FormControl(values));
              this.selectValues[formItem] = data;
            });
          break;
        default:
          this.editForm.addControl(formItem, new FormControl(this.entity[formItem]));
          break;
      }
    })
  }

  formKeys() {
    return Object.keys(this.formTemplate);
  }

  onCancel() {
    this.dialogRef.close();
  }

  onSubmit() {
    console.log(this.editForm.value);
    this.service.patch(this.entity.fromJson(this.editForm.value) as Entity).subscribe(resp => {
      this.dialogRef.close({ success: true });
      this.data.success = true;
    })
  }
}
