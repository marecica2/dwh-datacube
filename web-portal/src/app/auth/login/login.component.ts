import { Component, OnInit } from '@angular/core';
import { NgForm } from "@angular/forms";
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from "../auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  user = {
    username: "admin",
    password: "admin",
  };
  isLoading = false;
  error: any;

  constructor(
    private authService: AuthService,
    private snackBar: MatSnackBar,
  ) {
  }

  ngOnInit() {
  }

  onSubmit(form: NgForm) {
    if (!form.valid) {
      return;
    }
    const username = form.value.username;
    const password = form.value.password;
    this.isLoading = true;

    this.authService.login(username, password).subscribe(() => {
      this.isLoading = false;
    }, error => {
      this.error = error;
      this.snackBar.open(error, '', {
        panelClass: ['mat-toolbar', 'mat-warn'], // mat-accent mat-primary
        duration: 5000
      });
      this.isLoading = false;
      form.reset();
    });
  }
}
