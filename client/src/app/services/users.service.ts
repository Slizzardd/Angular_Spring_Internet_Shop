import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { IUser } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  private baseURL = 'http://localhost:8080/auth/user/';

  constructor(private httpClient: HttpClient) { }

  createUser(user: IUser): Observable<IUser> {
    return this.httpClient.post<IUser>(`${this.baseURL}createUser`, user);
  }

  loginUser(user: IUser): Observable<{ jwtToken: string }> {
    return this.httpClient.post<{ jwtToken: string }>(`${this.baseURL}login`, user).pipe(
      tap(({ jwtToken }) => {
        localStorage.setItem('auth-token', jwtToken);
      })
    );
  }

  getActualUser(): Observable<IUser> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('auth-token')}`
    });

    return this.httpClient.get<IUser>(`${this.baseURL}getUserByToken`, { headers });
  }
}
