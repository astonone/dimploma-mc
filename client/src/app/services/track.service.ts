import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {SharedService} from './shared.service';

@Injectable({
  providedIn: 'root'
})
export class TrackService {
  HOST: string = 'http://localhost';
  PORT: string = '8082';
  SERVER_URL: string;

  UPLOAD_TRACK: string;
  GET_ALL_TRACKS: string;

  constructor(private http : HttpClient,
              private shared : SharedService) {
    this.SERVER_URL = this.HOST + ':' + this.PORT;

    this.UPLOAD_TRACK = this.SERVER_URL + '/api/track/upload';
    this.GET_ALL_TRACKS = this.SERVER_URL + '/api/track/findAllPagination?page={page}&pageSize={pageSize}';
  }

  private getOptions() {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + this.shared.getStogare().getItem('token')
    });

    return { headers: headers };
  }

  upload(file : any) {
    return this.http.post<Observable<Object>>(this.UPLOAD_TRACK, file, this.getOptions())
  }

  getAllTracks(page: number, pageSize: number) {
    let regExp = /{page}/gi;
    let regExp2 = /{pageSize}/gi;
    let url = this.GET_ALL_TRACKS.replace(regExp, page + "");
    url = url.replace(regExp2, pageSize + "");
    return this.http.get<Observable<Object>>(url, this.getOptions())
  }
}
