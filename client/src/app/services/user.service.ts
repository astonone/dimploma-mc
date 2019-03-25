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
  USER_GET: string;
  USER_GET_ALL: string;
  USER_CREATE: string;
  USER_UPDATE: string;
  USER_ADD_USER_DATA: string;
  USER_UPLOAD_PHOTHO: string;

  constructor(private http : HttpClient) {
    this.SERVER_URL = this.HOST + ':' + this.PORT;

    this.USER_LOGIN = this.SERVER_URL + '/api/user/login';
    this.USER_AUTH = this.SERVER_URL + '/api/user/auth';
    this.USER_GET = this.SERVER_URL + '/api/user/email/';
    this.USER_GET_ALL = this.SERVER_URL + '/api/user/getAll?page={page}&pageSize={pageSize}';
    this.USER_CREATE = this.SERVER_URL + '/api/user/create';
    this.USER_UPDATE = this.SERVER_URL + '/api/user/update';
    this.USER_ADD_USER_DATA = this.SERVER_URL + '/api/user/{id}/user_details';
    this.USER_UPLOAD_PHOTHO = this.SERVER_URL + '/api/user/{id}/upload';
  }

  private getOptions() {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + sessionStorage.getItem('token')
    });

    return { headers: headers };
  }

  login(email:string, password: string) {
    return this.http.post<Observable<boolean>>(this.USER_LOGIN, {
      email,
      password
    })
  }

  auth() {
    return this.http.post<Observable<Object>>(this.USER_AUTH, {}, this.getOptions())
  }

  getUserByEmail(email:string) {
    return this.http.get<Observable<Object>>(this.USER_GET + email,  this.getOptions())
  }

  createUser(email:string, password: string) {
    return this.http.post<Observable<Object>>(this.USER_CREATE + "?email=" + email + "&password=" + password, this.getOptions())
  }

  addUserDetails(id, request) {
    var regExp = /{id}/gi;
    let url = this.USER_ADD_USER_DATA.replace(regExp, id);
    return this.http.put<Observable<Object>>(url, request, this.getOptions())
  }

  getAllUsers(page: number, pageSize: number) {
    let p = page + "";
    let ps = pageSize + "";
    let regExp = /{page}/gi;
    let regExp2 = /{pageSize}/gi;
    let url = this.USER_GET_ALL.replace(regExp, p);
    url = url.replace(regExp2, ps);
    return this.http.get<Observable<Object>>(url, this.getOptions())
  }

  updateUser(user:any) {
    return this.http.post<Observable<Object>>(this.USER_UPDATE, user, this.getOptions())
  }

  uploadPhoto(id:number, file:any) {
    let i = id + "";
    let regExp = /{id}/gi;
    let url = this.USER_UPLOAD_PHOTHO.replace(regExp, i);
    return this.http.post<Observable<Object>>(url, file, this.getOptions())
  }
}
