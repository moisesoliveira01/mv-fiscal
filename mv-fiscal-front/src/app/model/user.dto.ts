export class UserDTO {
  public id: number;
  public name: string;
  public email: string;
  public createdDate: Date;

  constructor(id: number, name: string, email: string, createdDate: Date) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.createdDate = createdDate;
  }
}
