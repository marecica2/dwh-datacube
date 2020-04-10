import { Component, Inject, OnInit } from "@angular/core";
import { MAT_DIALOG_DATA } from "@angular/material/dialog";
import { NgForm } from "@angular/forms";

import { ColumnDefinition, ColumnType, CrudRepositoryServiceImpl } from "../crudRepositoryApi";
import { tap } from "rxjs/operators";

@Component({
  selector: 'edit-dialog',
  templateUrl: './edit-dialog.component.html',
  styleUrls: ['./edit-dialog.component.css']
})
export class EditDialogComponent<Entity> implements OnInit {
  private json = JSON;
  private columnType = ColumnType;
  private formTemplate: ColumnDefinition;
  private entity: Entity;
  private selectValues = {};

  constructor(@Inject(MAT_DIALOG_DATA) public data: { entity: Entity, formTemplate: ColumnDefinition }) {
    this.formTemplate = data.formTemplate;
    this.entity = data.entity;
  }

  ngOnInit(): void {
    const selects = Object.entries(this.formTemplate).filter(([key, val]) => val.type == ColumnType.MULTI_SELECT || val.type == ColumnType.SELECT);
    selects.forEach(([key, val]) => val.service
      .findAll()
      .pipe(
        tap(data => console.log(data)),
      )
      .subscribe(data => {
        this.selectValues[key] = data;
      }));
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
