import { Component } from "@angular/core";

@Component({
  selector: 'app-logo',
  template: `
    <span>
        <mat-icon>insert_chart_outlined</mat-icon>&nbsp;&nbsp;
        <span>DWH Portal</span>
    </span>
  `,
  styles: ['mat-icon { position: relative; top:8px; }'],
})
export class LogoComponent {

}
