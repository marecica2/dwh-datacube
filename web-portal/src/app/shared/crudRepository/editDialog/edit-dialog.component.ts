import { Component, Inject, OnInit } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormControl, FormGroup } from "@angular/forms";

import { ColumnDefinition, ColumnType, CrudResource, MultiSelectColumn } from "../crudRepositoryApi";
import { RestService } from "@lagoshny/ngx-hal-client";

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
  private modelType: { new(...args: any[]): Entity; };
  private isEditMode = true;

  constructor(
    public dialogRef: MatDialogRef<EditDialogComponent<Entity>>,
    @Inject(MAT_DIALOG_DATA) public data: {
      success: boolean;
      entity: Entity,
      formTemplate: ColumnDefinition,
      service: RestService<Entity>,
      modelType: { new(...args: any[]): Entity; },
    }
  ) {
    this.formTemplate = data.formTemplate;
    this.entity = data.entity;
    this.editForm = new FormGroup({});
    this.service = data.service;
    this.modelType = data.modelType;
    if (this.entity == null) {
      this.isEditMode = false;
      this.entity = new this.modelType();
    }
  }

  getFormItemType(object: Object): string {
    return object.constructor.name;
  }

  ngOnInit(): void {
    Object.keys(this.formTemplate).forEach((formItem: string) => {
      switch (this.getFormItemType(this.formTemplate[formItem])) {

        case ColumnType.MULTI_SELECT:
          const selectFormElement = this.formTemplate[formItem] as MultiSelectColumn;
          selectFormElement.service
            .getAll()
            .subscribe(data => {
              let values = null;
              if (this.isEditMode) {
                values = this.entity[formItem];
              }
              this.editForm.addControl(formItem, new FormControl(values));
              this.selectValues[formItem] = data;
            });
          break;

        default:
          let value = null;
          if (this.isEditMode) {
            value = this.entity[formItem];
          }
          this.editForm.addControl(formItem, new FormControl(value));
          break;
      }
    })
  }

  formKeys() {
    return Object.keys(this.formTemplate).filter(key => key != new this.modelType().getIdentityKey());
  }

  onCancel() {
    this.dialogRef.close();
  }

  onSubmit() {
    if(this.isEditMode) {
      this.service.patch({ ...this.entity, ...this.editForm.value }).subscribe(resp => {
        this.dialogRef.close({ success: true });
        this.data.success = true;
      })
    } else {
      this.service.create(this.editForm.value).subscribe(resp => {
        this.dialogRef.close({ success: true });
        this.data.success = true;
      })
    }
  }

  compareWith(e1: Entity, e2: Entity): boolean {
    if(e1 != null && e1._links !== null && e2 !== null && e2._links !== null) {
      return e2._links.self.href.indexOf(e1._links.self.href) != -1 || e1._links.self.href.indexOf(e2._links.self.href) != -1;
    }
    return false;
  }
}
