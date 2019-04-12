import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SharedService } from './shared.service';

@Injectable({
  providedIn: 'root'
})
export class TrackService {
  HOST: string = 'http://localhost';
  PORT: string = '8082';
  SERVER_URL: string;

  UPLOAD_TRACK: string;
  GET_ALL_TRACKS: string;
  GET_USER_TRACKS: string;
  ADD_TRACK_TO_USER: string;
  DELETE_TRACK_FROM_USER: string;
  DELETE_TRACK: string;
  RATE_TRACK: string;
  UPDATE_TRACK: string;
  GET_UPLOADED_TRACK: string;

  constructor(private http : HttpClient,
              private shared : SharedService) {
    this.SERVER_URL = this.shared.getServerURL();

    this.UPLOAD_TRACK = this.SERVER_URL + '/api/track/files/upload';
    this.GET_UPLOADED_TRACK = this.SERVER_URL + '/api/track/files/{filename}';
    this.GET_ALL_TRACKS = this.SERVER_URL + '/api/track/findAllPagination?page={page}&pageSize={pageSize}';
    this.GET_USER_TRACKS = this.SERVER_URL + '/api/track/getTracksByUser/{id}?page={page}&pageSize={pageSize}';
    this.ADD_TRACK_TO_USER = this.SERVER_URL + '/api/track/{id}/user?userId={userId}';
    this.DELETE_TRACK_FROM_USER = this.SERVER_URL + '/api/track/{id}/user?userId={userId}';
    this.DELETE_TRACK = this.SERVER_URL + '/api/track/{id}';
    this.RATE_TRACK = this.SERVER_URL + '/api/track/{id}/rating?ratingValue={ratingValue}&userId={userId}';
    this.UPDATE_TRACK = this.SERVER_URL + '/api/track/{id}/update';
  }

  private getOptions() {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + this.shared.getStorage().getItem('token')
    });

    return { headers: headers };
  }

  getAllTracks(page: number, pageSize: number) {
    let regExp = /{page}/gi;
    let regExp2 = /{pageSize}/gi;
    let url = this.GET_ALL_TRACKS.replace(regExp, page + "");
    url = url.replace(regExp2, pageSize + "");
    return this.http.get<Observable<Object>>(url, this.getOptions())
  }

  getUserTracks(id:number, page: number, pageSize: number) {
    let regExp = /{page}/gi;
    let regExp2 = /{pageSize}/gi;
    let regExp3 = /{id}/gi;
    let url = this.GET_USER_TRACKS.replace(regExp, page + "");
    url = url.replace(regExp2, pageSize + "");
    url = url.replace(regExp3, id + "");
    return this.http.get<Observable<Object>>(url, this.getOptions())
  }

  addTrackToUser(userId: number, trackId: number) {
    let regExp = /{id}/gi;
    let regExp2 = /{userId}/gi;
    let url = this.ADD_TRACK_TO_USER.replace(regExp, trackId + "");
    url = url.replace(regExp2, userId + "");
    return this.http.put<Observable<Object>>(url,{}, this.getOptions())
  }

  deleteTrackFromUser(userId: number, trackId: number) {
    let regExp = /{id}/gi;
    let regExp2 = /{userId}/gi;
    let url = this.DELETE_TRACK_FROM_USER.replace(regExp, trackId + "");
    url = url.replace(regExp2, userId + "");
    return this.http.delete<Observable<Object>>(url, this.getOptions())
  }

  deleteTrack(id: number) {
    let regExp = /{id}/gi;
    let url = this.DELETE_TRACK.replace(regExp, id + "");
    return this.http.delete<Observable<Object>>(url, this.getOptions());
  }

  rateTrack(trackId: number, userId: number, ratingValue: number) {
    let regExp = /{id}/gi;
    let regExp2 = /{ratingValue}/gi;
    let regExp3 = /{userId}/gi;
    let url = this.RATE_TRACK.replace(regExp, trackId + "");
    url = url.replace(regExp2, ratingValue + "");
    url = url.replace(regExp3, userId + "");
    return this.http.put<Observable<Object>>(url,{}, this.getOptions());
  }

  updateTrack(id: number, track : any) {
    let regExp = /{id}/gi;
    let url = this.UPDATE_TRACK.replace(regExp, id + "");
    return this.http.post<Observable<Object>>(url, track, this.getOptions());
  }
}
