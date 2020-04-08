import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { CrudRepositoryService } from "../../shared/crudRepository/crudRepositoryApi";
import { Tenant } from "./tenant.model";

@Injectable({
  providedIn: 'root',
})
export class TenantService extends CrudRepositoryService<Tenant> {
  private static baseUrl = '/api/security/tenants';

  constructor(http: HttpClient) {
    super(http, TenantService.baseUrl)
  }
}
