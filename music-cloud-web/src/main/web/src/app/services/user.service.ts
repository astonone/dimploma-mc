import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../dto/user';
import { SharedService } from './shared.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private SERVER_URL: string;

  private USER_LOGIN : string;
  private USER_AUTH : string;
  private USER_GET: string;
  private USER_DELETE: string;
  private USER_GET_ALL: string;
  private USER_CREATE: string;
  private USER_UPDATE: string;
  private USER_ADD_USER_DATA: string;
  private USER_UPLOAD_PHOTO: string;
  private USER_DELETE_PHOTO: string;
  private USER_UPDATE_INFO: string;
  private USER_GET_BY_ID: string;
  private GET_UPLOADED_PHOTO: string;
  private SEND_FRIEND_REQUEST: string;
  private CANCEL_FRIEND_REQUEST: string;
  private ADD_FRIEND_REQUEST: string;
  private REMOVE_FRIEND_REQUEST: string;
  private GET_FRIEND_REQUESTS: string;
  private GET_FRIENDS: string;
  private GET_USERS: string;

  constructor(private http : HttpClient,
              private shared : SharedService) {
    this.SERVER_URL = this.shared.getServerURL();

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
    this.SEND_FRIEND_REQUEST  = this.SERVER_URL + '/api/user/{id}/sendFriendRequest?friendId={friendId}';
    this.CANCEL_FRIEND_REQUEST  = this.SERVER_URL + '/api/user/{id}/cancelFriendRequest?friendId={friendId}';
    this.ADD_FRIEND_REQUEST  = this.SERVER_URL + '/api/user/{id}/applyFriendRequest?inviterId={inviterId}';
    this.REMOVE_FRIEND_REQUEST  = this.SERVER_URL + '/api/user/{id}/removeFriend?friendId={friendId}';
    this.GET_FRIEND_REQUESTS  = this.SERVER_URL + '/api/user/{id}/requests';
    this.GET_FRIENDS = this.SERVER_URL + '/api/user/{id}/friends';
    this.GET_USERS = this.SERVER_URL + '/api/user/find?page={page}&pageSize={pageSize}&firstName={firstName}&lastName={lastName}&nickName={nickName}';
  }

  private getOptions() {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + this.shared.getStorage().getItem('token')
    });

    return { headers: headers };
  }

  sendFriendRequest(userId: number, friendId: number) {
    let regExp = /{id}/gi;
    let regExp2 = /{friendId}/gi;
    let url = this.SEND_FRIEND_REQUEST.replace(regExp, userId.toString());
    url = url.replace(regExp2, friendId.toString());
    return this.http.post<Observable<Object>>(url, {}, this.getOptions());
  }

  cancelFriendRequest(userId: number, friendId: number) {
    let regExp = /{id}/gi;
    let regExp2 = /{friendId}/gi;
    let url = this.CANCEL_FRIEND_REQUEST.replace(regExp, userId.toString());
    url = url.replace(regExp2, friendId.toString());
    return this.http.post<Observable<Object>>(url, {}, this.getOptions());
  }

  addFriend(userId: number, inviterId: number) {
    let regExp = /{id}/gi;
    let regExp2 = /{inviterId}/gi;
    let url = this.ADD_FRIEND_REQUEST.replace(regExp, userId.toString());
    url = url.replace(regExp2, inviterId.toString());
    return this.http.post<Observable<Object>>(url, {}, this.getOptions());
  }

  removeFriend(userId: number, friendId: number) {
    let regExp = /{id}/gi;
    let regExp2 = /{friendId}/gi;
    let url = this.REMOVE_FRIEND_REQUEST.replace(regExp, userId.toString());
    url = url.replace(regExp2, friendId.toString());
    return this.http.post<Observable<Object>>(url, {}, this.getOptions());
  }

  getAllFriendRequests(userId: number) {
    let regExp = /{id}/gi;
    let url = this.GET_FRIEND_REQUESTS.replace(regExp, userId.toString());
    return this.http.get<Observable<Object>>(url, this.getOptions());
  }

  getAllFriends(userId: number) {
    let regExp = /{id}/gi;
    let url = this.GET_FRIENDS.replace(regExp, userId.toString());
    return this.http.get<Observable<Object>>(url, this.getOptions());
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
    let url = this.USER_GET_ALL.replace(regExp, page.toString());
    url = url.replace(regExp2, pageSize + "");
    return this.http.get<Observable<Object>>(url, this.getOptions())
  }

  findUsers(page: number, pageSize: number, firstName: string, lastName: string, nickName: string) {
    let regExp = /{page}/gi;
    let regExp2 = /{pageSize}/gi;
    let regExp3 = /{firstName}/gi;
    let regExp4 = /{lastName}/gi;
    let regExp5 = /{nickName}/gi;
    let url = this.GET_USERS.replace(regExp, page.toString());
    url = url.replace(regExp2, pageSize.toString());
    url = url.replace(regExp3, firstName);
    url = url.replace(regExp4, lastName);
    url = url.replace(regExp5, nickName);
    return this.http.get<Observable<Object>>(url, this.getOptions())
  }

  updateUser(user:any) {
    return this.http.post<Observable<Object>>(this.USER_UPDATE, user, this.getOptions())
  }

  deletePhoto(id:number) {
    let regExp = /{id}/gi;
    let url = this.USER_DELETE_PHOTO.replace(regExp, id.toString());
    return this.http.post<Observable<Object>>(url, {},this.getOptions());
  }

  updateUserInfo(user:User) {
    let regExp = /{id}/gi;
    let url = this.USER_UPDATE_INFO.replace(regExp, user.id.toString());
    return this.http.put<Observable<Object>>(url, user.userDetails.toObject(), this.getOptions())
  }

  deleteUser(id:number) {
    let regExp = /{id}/gi;
    let url = this.USER_DELETE.replace(regExp, id.toString());
    return this.http.delete<Observable<Object>>(url, this.getOptions())
  }

  getById(id:string) {
    let regExp = /{id}/gi;
    let url = this.USER_GET_BY_ID.replace(regExp, id);
    return this.http.get<Observable<Object>>(url, this.getOptions())
  }
}
