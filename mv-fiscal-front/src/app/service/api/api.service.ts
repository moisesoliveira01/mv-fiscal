import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export abstract class ApiService {
  constructor(protected http: HttpClient, protected apiBaseUrl: string) {}

  get<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`${this.apiBaseUrl}/${endpoint}`);
  }

  getWithUrlParams<T>(
    endpoint: string,
    params: Record<string, string | number>
  ): Observable<T> {
    const queryParams = new URLSearchParams();

    Object.entries(params).forEach(([key, value]) => {
      queryParams.set(key, value ? value.toString() : '');
    });

    const url = `${this.apiBaseUrl}/${endpoint}?${queryParams.toString()}`;

    return this.http.get<T>(url);
  }

  post<T>(endpoint: string, data: any): Observable<T> {
    return this.http.post<T>(`${this.apiBaseUrl}/${endpoint}`, data);
  }

  put<T>(endpoint: string, data: any): Observable<T> {
    return this.http.put<T>(`${this.apiBaseUrl}/${endpoint}`, data);
  }

  delete<T>(endpoint: string, id: number | string): Observable<T> {
    return this.http.delete<T>(`${this.apiBaseUrl}/${endpoint}/${id}`);
  }
}
