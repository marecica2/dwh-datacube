import { Component } from "@angular/core";

@Component({
  selector: 'app-logo',
  template: `
    <div class="aligned">
        <mat-icon class="mat-icon-large">insert_chart_outlined</mat-icon>&nbsp;&nbsp;
        <span>DWH Portal</span>
    </div>
  `,
  styles: ['mat-icon {  }'],
})
export class LogoComponent {

}
