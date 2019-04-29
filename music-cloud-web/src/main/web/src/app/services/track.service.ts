import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SharedService } from './shared.service';

@Injectable({
  providedIn: 'root'
})
export class TrackService {
  private SERVER_URL: string;

  private UPLOAD_TRACK: string;
  private GET_ALL_TRACKS: string;
  private GET_USER_TRACKS: string;
  private ADD_TRACK_TO_USER: string;
  private DELETE_TRACK_FROM_USER: string;
  private DELETE_TRACK: string;
  private RATE_TRACK: string;
  private UPDATE_TRACK: string;
  private GET_UPLOADED_TRACK: string;
  private GET_RECOMMENDED_TRACKS: string;
  private FIND_TRACKS: string;
  private GET_TRACK: string;

  constructor(private http: HttpClient,
              private shared: SharedService) {
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
    this.GET_RECOMMENDED_TRACKS = this.SERVER_URL + '/api/recommend/tracksForUser/{id}?nBestUsers=10&nBestTracks=10';
    this.FIND_TRACKS = this.SERVER_URL + '/api/track/find?page={page}&pageSize={pageSize}';
    this.GET_TRACK = this.SERVER_URL + '/api/track/{id}';
  }

  private getOptions() {
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + this.shared.getStorage().getItem('token')
    });

    return { headers: headers };
  }

  public getAllTracks(page: number, pageSize: number) {
    const regExp = /{page}/gi;
    const regExp2 = /{pageSize}/gi;
    let url = this.GET_ALL_TRACKS.replace(regExp, page.toString());
    url = url.replace(regExp2, pageSize.toString());
    return this.http.get<Observable<Object>>(url, this.getOptions());
  }

  public findTracks(request: any, page: number, pageSize: number) {
    const regExp = /{page}/gi;
    const regExp2 = /{pageSize}/gi;
    let url = this.FIND_TRACKS.replace(regExp, page.toString());
    url = url.replace(regExp2, pageSize.toString());
    return this.http.post<Observable<Object>>(url, request, this.getOptions());
  }

  public getUserTracks(id: number, page: number, pageSize: number) {
    const regExp = /{page}/gi;
    const regExp2 = /{pageSize}/gi;
    const regExp3 = /{id}/gi;
    let url = this.GET_USER_TRACKS.replace(regExp, page.toString());
    url = url.replace(regExp2, pageSize.toString());
    url = url.replace(regExp3, id.toString());
    return this.http.get<Observable<Object>>(url, this.getOptions());
  }

  public addTrackToUser(userId: number, trackId: number) {
    const regExp = /{id}/gi;
    const regExp2 = /{userId}/gi;
    let url = this.ADD_TRACK_TO_USER.replace(regExp, trackId.toString());
    url = url.replace(regExp2, userId.toString());
    return this.http.put<Observable<Object>>(url, {}, this.getOptions());
  }

  public deleteTrackFromUser(userId: number, trackId: number) {
    const regExp = /{id}/gi;
    const regExp2 = /{userId}/gi;
    let url = this.DELETE_TRACK_FROM_USER.replace(regExp, trackId.toString());
    url = url.replace(regExp2, userId.toString());
    return this.http.delete<Observable<Object>>(url, this.getOptions());
  }

  public getTrack(id: number) {
    const regExp = /{id}/gi;
    const url = this.GET_TRACK.replace(regExp, id.toString());
    return this.http.get<Observable<Object>>(url, this.getOptions());
  }

  public deleteTrack(id: number) {
    const regExp = /{id}/gi;
    const url = this.DELETE_TRACK.replace(regExp, id.toString());
    return this.http.delete<Observable<Object>>(url, this.getOptions());
  }

  public rateTrack(trackId: number, userId: number, ratingValue: number) {
    const regExp = /{id}/gi;
    const regExp2 = /{ratingValue}/gi;
    const regExp3 = /{userId}/gi;
    let url = this.RATE_TRACK.replace(regExp, trackId.toString());
    url = url.replace(regExp2, ratingValue.toString());
    url = url.replace(regExp3, userId.toString());
    return this.http.put<Observable<Object>>(url, {}, this.getOptions());
  }

  public updateTrack(id: number, track: any) {
    const regExp = /{id}/gi;
    const url = this.UPDATE_TRACK.replace(regExp, id.toString());
    return this.http.post<Observable<Object>>(url, track, this.getOptions());
  }

  public getRecommendedUserTracks(id: number) {
    const regExp = /{id}/gi;
    const url = this.GET_RECOMMENDED_TRACKS.replace(regExp, id.toString());
    return this.http.get<Observable<Object>>(url, this.getOptions());
  }
}
