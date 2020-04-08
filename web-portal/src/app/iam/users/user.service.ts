import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface UsersApiResponse {
  _embedded: { users: User[] };
  page: Page;
}

export interface User {
  username: string;
  firstName: string;
  lastName: string;
  email: string;
}

export interface Page {
  size: number,
  totalElements: number,
  totalPages: number,
  number: number
}

@Injectable({
  providedIn: 'root',
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  getUsers(sort: string, order: string, page: number): Observable<UsersApiResponse> {
    const href = '/api/security/users';
    const requestUrl = `${href}?sort=${sort}&order=${order}&page=${page}&size=5`;
    return this.http.get<UsersApiResponse>(requestUrl);
  }
}
