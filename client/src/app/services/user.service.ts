import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  HOST: string = 'http://localhost';
  PORT: string = '8082';
  SERVER_URL: string;

  USER_LOGIN : string;
  USER_AUTH : string;

  constructor(private http : HttpClient) {
    this.SERVER_URL = this.HOST + ':' + this.PORT;

    this.USER_LOGIN = this.SERVER_URL + '/api/user/login';
    this.USER_AUTH = this.SERVER_URL + '/api/user/auth';
  }

  login(email:string, password: string) {
    return this.http.post<Observable<boolean>>(this.USER_LOGIN, {
      email,
      password
    })
  }

  auth() {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + sessionStorage.getItem('token')
    });

    let options = { headers: headers };
    return this.http.post<Observable<Object>>(this.USER_AUTH, {}, options)
  }
}
