import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TrackService {
  HOST: string = 'http://localhost';
  PORT: string = '8082';
  SERVER_URL: string;

  UPLOAD_TRACK: string;

  constructor(private http : HttpClient) {
    this.SERVER_URL = this.HOST + ':' + this.PORT;

    this.UPLOAD_TRACK = this.SERVER_URL + '/api/track/upload';
  }

  private getOptions() {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + sessionStorage.getItem('token')
    });

    return { headers: headers };
  }

  upload(file : any) {
    return this.http.post<Observable<Object>>(this.UPLOAD_TRACK, file, this.getOptions())
  }
}
