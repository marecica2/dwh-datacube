import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ExternalConfigurationHandlerInterface, ExternalConfiguration } from '@lagoshny/ngx-hal-client';

@Injectable()
export class ExternalConfigurationService implements ExternalConfigurationHandlerInterface {

  getProxyUri(): string {
    return '/api/security/';
  }

  getRootUri(): string {
    return '/api/security/';
  }

  getHttp(): HttpClient {
    return this.http;
  }

  constructor(private http: HttpClient) {
  }

  getExternalConfiguration(): ExternalConfiguration {
    return null;
  }

  setExternalConfiguration(externalConfiguration: ExternalConfiguration) {
  }

  deserialize(): any {
  }

  serialize(): any {
  }
}
