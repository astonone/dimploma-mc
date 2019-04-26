import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SharedService } from './shared.service';

@Injectable({
  providedIn: 'root'
})
export class PlaylistService {
  private SERVER_URL: string;

  private CREATE_PLAYLIST: string;
  private DELETE_PLAYLIST: string;
  private GET_PLAYLIST: string;

  constructor(private http : HttpClient,
              private shared : SharedService) {
    this.SERVER_URL = this.shared.getServerURL();

    this.CREATE_PLAYLIST = this.SERVER_URL + 'api/playlist/create?name={name}';
    this.GET_PLAYLIST = this.SERVER_URL + '/api/playlist/{id}';
    this.DELETE_PLAYLIST = this.SERVER_URL + '/api/playlist/{id}';
  }

  private getOptions() {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + this.shared.getStorage().getItem('token')
    });

    return { headers: headers };
  }
}
