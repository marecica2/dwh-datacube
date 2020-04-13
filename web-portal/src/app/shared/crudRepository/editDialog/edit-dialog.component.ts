import {Component, Inject, OnInit} from "@angular/core";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FormControl, FormGroup, NgForm} from "@angular/forms";

import {ColumnDefinition, ColumnType} from "../crudRepositoryApi";
import {tap} from "rxjs/operators";

@Component({
  selector: 'edit-dialog',
  templateUrl: './edit-dialog.component.html',
  styleUrls: ['./edit-dialog.component.css']
})
export class EditDialogComponent<Entity> implements OnInit {
  json = JSON;
  columnType = ColumnType;
  formTemplate: ColumnDefinition;
  entity: Entity;
  selectValues = {};
  editForm: FormGroup;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { entity: Entity, formTemplate: ColumnDefinition }) {
    this.formTemplate = data.formTemplate;
    this.entity = data.entity;
    this.editForm = new FormGroup({});
  }

  getType(object: Object): string {
    return object.constructor.name;
  }

  compare(a,b) {
    console.log(a, b);
  }

  compareWith(key: string) {
    return (obj1: any, obj2: any) => {
      console.log(key, obj1, obj2)
      return false;
    }
  }

  ngOnInit(): void {
    Object.keys(this.formTemplate).forEach((formItem: string) => {
      switch (this.getType(this.formTemplate[formItem])) {
        case ColumnType.SELECT:
          this.formTemplate[formItem].service
            .findAll()
            .subscribe(data => {
              const attribute = this.formTemplate[formItem].value;
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
    console.log(this.editForm);
  }
}
