import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { CrudRepositoryServiceImpl } from "../../shared/crudRepository/crudRepositoryApi";
import { Tenant } from "./tenant.model";

@Injectable({
  providedIn: 'root',
})
export class TenantService extends CrudRepositoryServiceImpl<Tenant> {
  private static baseUrl = '/api/security/tenants';

  constructor(http: HttpClient) {
    super(http, TenantService.baseUrl, 'tenants')
  }
}
