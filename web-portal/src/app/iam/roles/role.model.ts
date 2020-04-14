import {CrudEntity} from "../../shared/crudRepository/crudRepositoryApi";

export class Role implements CrudEntity<Role>{
  constructor(
    public id: number,
    public name: string,
    public description: string,
    public createdOn: number,
    public modifiedOn: number,
  ) {
  }

  getId(): any {
    return this.id;
  }

  fromJson(data: object): Role {
    return undefined;
  }
}
