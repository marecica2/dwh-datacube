import { Component } from '@angular/core';
import { AbstractCrudTableComponent } from "../../shared/crudRepository/abstractCrudTable.component";
import { TenantService } from "./tenant.service";
import { Tenant } from "./tenant.model";

@Component({
  selector: 'iam-tenants-table',
  templateUrl: '../../shared/crudRepository/abstractCrudTable.component.html',
  styleUrls: ['../../shared/crudRepository/abstractCrudTable.component.css']
})
export class TenantsTableComponent extends AbstractCrudTableComponent<Tenant> {

  constructor(private tenantService: TenantService) {
    super(tenantService);
  }

  columnDefinition(): string[] {
    return ['id', 'schemaName'];
  }

  relation(): string {
    return "tenants";
  }
}
