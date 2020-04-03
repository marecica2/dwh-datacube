import { Injectable } from "@angular/core";
import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable, Subscription } from "rxjs";

import { AuthService, Token } from "./auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private tokenSub: Subscription;
  private token: Token;

  constructor(private authService: AuthService) {
    this.tokenSub = this.authService.tokenSubject.subscribe((token: Token) => {
      if(token !== null) {
        this.token = token;
      }
    });
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.url.indexOf("/oauth/token") !== -1) {
      return next.handle(req);
    }
    let headers = new HttpHeaders();
    headers = headers.append('Authorization', `Bearer ${this.token.access_token}`);
    const modifiedReq = req.clone({ headers });
    return next.handle(modifiedReq);
  }

}
