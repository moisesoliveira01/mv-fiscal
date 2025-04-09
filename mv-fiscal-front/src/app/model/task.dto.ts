export class TaskDTO {
  public id: number;
  public title: string;
  public description: string;
  public status: string;
  public createdDate: Date;
  public limitDate: Date;
  public userId: number;

  constructor(
    id: number,
    title: string,
    description: string,
    status: string,
    createdDate: Date,
    limitDate: Date,
    userId: number
  ) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.createdDate = createdDate;
    this.limitDate = limitDate;
    this.userId = userId;
  }
}
