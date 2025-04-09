import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';
import { environment } from '../../../environment/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaskDTO } from '../../model/task.dto';

@Injectable({
  providedIn: 'root',
})
export class TaskService extends ApiService {
  private resourcePath = 'tarefa';

  constructor(protected override http: HttpClient) {
    super(http, environment.taskApiBaseUrl);
  }

  public getPageByFilter(
    id: number,
    title: string,
    description: string,
    status: string,
    createdDate: Date,
    limitDate: Date,
    userId: number,
    pageNumber: number,
    pageSize: number
  ): Observable<any> {
    const params: any = {
      id: id,
      title: title,
      description: description,
      status: status,
      createdDate: createdDate,
      limitDate: limitDate,
      userId: userId,
      page: pageNumber,
      size: pageSize,
    };

    return this.getWithUrlParams(`${this.resourcePath}`, params);
  }

  public save(taskDTO: TaskDTO): Observable<any> {
    if (taskDTO.id) {
      return this.put(`${this.resourcePath}`, taskDTO);
    }
    return this.post(`${this.resourcePath}`, taskDTO);
  }

  public deleteById(taskId: number) {
    return this.delete(`${this.resourcePath}`, taskId);
  }
}
