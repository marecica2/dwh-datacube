export class User {
  public id: number;
  public firstName: string;
  public lastName: string;
  public username: string;
  public email: string;
  public roles: string[];
  public tenants: object[];
  public token: string;
  public refreshToken: string;
  public expiresIn: number;

  constructor(
  ) {}
}
