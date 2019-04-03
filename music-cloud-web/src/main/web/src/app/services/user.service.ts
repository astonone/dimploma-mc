import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../dto/user';
import { SharedService } from './shared.service';

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
  USER_DELETE: string;
  USER_GET_ALL: string;
  USER_CREATE: string;
  USER_UPDATE: string;
  USER_ADD_USER_DATA: string;
  USER_UPLOAD_PHOTO: string;
  USER_DELETE_PHOTO: string;
  USER_UPDATE_INFO: string;
  USER_GET_BY_ID: string;
  GET_UPLOADED_PHOTO: string;

  constructor(private http : HttpClient,
              private shared : SharedService) {
    this.SERVER_URL = this.HOST + ':' + this.PORT;

    this.USER_LOGIN = this.SERVER_URL + '/api/user/login';
    this.USER_AUTH = this.SERVER_URL + '/api/user/auth';
    this.USER_GET = this.SERVER_URL + '/api/user/email/';
    this.USER_GET_BY_ID = this.SERVER_URL + '/api/user/{id}';
    this.USER_DELETE = this.SERVER_URL + '/api/user/{id}';
    this.USER_GET_ALL = this.SERVER_URL + '/api/user/getAll?page={page}&pageSize={pageSize}';
    this.USER_CREATE = this.SERVER_URL + '/api/user/create';
    this.USER_UPDATE = this.SERVER_URL + '/api/user/update';
    this.USER_ADD_USER_DATA = this.SERVER_URL + '/api/user/{id}/user_details';
    this.USER_UPLOAD_PHOTO = this.SERVER_URL + '/api/user/{id}/upload';
    this.USER_DELETE_PHOTO = this.SERVER_URL + '/api/user/{id}/deletePhoto';
    this.USER_UPDATE_INFO = this.SERVER_URL + '/api/user/{id}/user_details';
    this.GET_UPLOADED_PHOTO  = this.SERVER_URL + '/api/user/files/{filename}';
  }

  private getOptions() {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + this.shared.getStorage().getItem('token')
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
    return this.http.post<Observable<User>>(this.USER_AUTH, {}, this.getOptions())
  }

  createUser(email:string, password: string) {
    return this.http.post<Observable<Object>>(this.USER_CREATE + "?email=" + email + "&password=" + password, this.getOptions())
  }

  addUserDetails(id, request) {
    var regExp = /{id}/gi;
    let url = this.USER_ADD_USER_DATA.replace(regExp, id);
    return this.http.put<Observable<Object>>(url, request)
  }

  getAllUsers(page: number, pageSize: number) {
    let regExp = /{page}/gi;
    let regExp2 = /{pageSize}/gi;
    let url = this.USER_GET_ALL.replace(regExp, page + "");
    url = url.replace(regExp2, pageSize + "");
    return this.http.get<Observable<Object>>(url, this.getOptions())
  }

  updateUser(user:any) {
    return this.http.post<Observable<Object>>(this.USER_UPDATE, user, this.getOptions())
  }

  deletePhoto(id:number) {
    let regExp = /{id}/gi;
    let url = this.USER_DELETE_PHOTO.replace(regExp, id + "");
    return this.http.post<Observable<Object>>(url, {},this.getOptions());
  }

  updateUserInfo(user:User) {
    let regExp = /{id}/gi;
    let url = this.USER_UPDATE_INFO.replace(regExp, user.id + "");
    return this.http.put<Observable<Object>>(url, user.userDetails.toObject(), this.getOptions())
  }

  deleteUser(id:number) {
    let regExp = /{id}/gi;
    let url = this.USER_DELETE.replace(regExp, id + "");
    return this.http.delete<Observable<Object>>(url, this.getOptions())
  }

  getById(id:string) {
    let regExp = /{id}/gi;
    let url = this.USER_GET_BY_ID.replace(regExp, id);
    return this.http.get<Observable<Object>>(url, this.getOptions())
  }
}
