import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { AbstractCrudRepositoryService } from "../../shared/crudRepository/crudRepositoryApi";
import { Tenant } from "./tenant.model";

@Injectable({
  providedIn: 'root',
})
export class TenantService extends AbstractCrudRepositoryService<Tenant> {
  private static baseUrl = '/api/security/tenants';

  constructor(http: HttpClient) {
    super(http, TenantService.baseUrl)
  }
}
