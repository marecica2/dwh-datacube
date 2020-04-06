import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgForm } from "@angular/forms";
import { Subscription } from "rxjs";
import { AuthService } from "../auth.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-tenant',
  templateUrl: './tenant.component.html',
  styleUrls: ['./tenant.component.css']
})
export class TenantComponent implements OnInit, OnDestroy {
  isLoading: false;
  tenants = [];
  private tenantSub: Subscription;

  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.tenantSub = this.authService.userSubject.subscribe(user => {
      if (user) {
        this.tenants = user.tenants;
      }
    });
  }

  ngOnDestroy(): void {
    this.tenantSub.unsubscribe();
  }

  onSubmit({ value }) {
    const selectedTenant = this.tenants
      .find(tenant => tenant.id === value);
    this.authService.selectTenant(selectedTenant);
    this.router.navigate(['/apps'], { queryParamsHandling: "merge" });
  }

}
