import { Component, Inject, OnInit } from "@angular/core";
import { MAT_DIALOG_DATA } from "@angular/material/dialog";
import { FormControl, FormGroup } from "@angular/forms";

import { ColumnDefinition, ColumnType, CrudEntity, CrudRepositoryService, SelectColumn } from "../crudRepositoryApi";

@Component({
  selector: 'edit-dialog',
  templateUrl: './edit-dialog.component.html',
  styleUrls: ['./edit-dialog.component.css']
})
export class EditDialogComponent<Entity extends CrudEntity<Entity>> implements OnInit {
  public json = JSON;
  public columnType = ColumnType;
  public formTemplate: ColumnDefinition;
  public entity: Entity;
  public selectValues = {};
  public editForm: FormGroup;
  private service: CrudRepositoryService<Entity>;

  constructor(@Inject(
    MAT_DIALOG_DATA) public data: { entity: Entity, formTemplate: ColumnDefinition, service: CrudRepositoryService<Entity> }) {
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
            .findAll()
            .subscribe(data => {
              const attribute = selectFormElement.value;
              const values = this.entity[formItem].map(val => val[attribute]);
              console.log(values);
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

  onSubmit() {
    this.service.update(this.entity.getId(), this.editForm.value).subscribe(resp => {
      console.log(resp);
    })
  }
}
