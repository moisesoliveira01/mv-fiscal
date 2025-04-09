import { Injectable } from '@angular/core';
import { UserDTO } from '../../model/user.dto';
import { Observable } from 'rxjs';
import { ApiService } from '../api/api.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService extends ApiService {
  private resourcePath = 'usuario';

  constructor(protected override http: HttpClient) {
    super(http, environment.userApiBaseUrl);
  }

  public getPageByFilter(
    id: number | null,
    name: string,
    email: string,
    createdDate: Date | null,
    pageNumber: number,
    pageSize: number
  ): Observable<any> {
    const params: any = {
      id: id,
      name: name,
      email: email,
      createdDate: createdDate,
      page: pageNumber,
      size: pageSize,
    };

    return this.getWithUrlParams(`${this.resourcePath}`, params);
  }

  public save(userDTO: UserDTO): Observable<any> {
    if (userDTO.id) {
      return this.put(`${this.resourcePath}`, userDTO);
    }
    return this.post(`${this.resourcePath}`, userDTO);
  }

  public deleteById(userId: number) {
    return this.delete(`${this.resourcePath}`, userId);
  }
}
