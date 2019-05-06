import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SharedService } from './shared.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PlaylistService {
  private SERVER_URL: string;

  private CREATE_PLAYLIST: string;
  private DELETE_PLAYLIST: string;
  private GET_PLAYLIST: string;
  private GET_ALL_PLAYLISTS_WITH_TRACKS: string;
  private ADD_TRACK_IN_PLAYLIST: string;
  private REMOVE_TRACK_FROM_PLAYLIST: string;

  constructor(private http: HttpClient,
              private shared: SharedService) {
    this.SERVER_URL = this.shared.getServerURL();

    this.CREATE_PLAYLIST = this.SERVER_URL + '/api/playlist/create?name={name}&userId={userId}';
    this.DELETE_PLAYLIST = this.SERVER_URL + '/api/playlist/{id}';
    this.GET_PLAYLIST = this.SERVER_URL + '/api/playlist/{id}/showTracks';
    this.GET_ALL_PLAYLISTS_WITH_TRACKS = this.SERVER_URL + '/api/playlist/all/showTracks';
    this.ADD_TRACK_IN_PLAYLIST = this.SERVER_URL + '/api/playlist/{id}/track?trackId={trackId}';
    this.REMOVE_TRACK_FROM_PLAYLIST = this.SERVER_URL + '/api/playlist/{id}/track?trackId={trackId}';
  }

  private getOptions() {
    const headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + this.shared.getStorage().getItem('token')
    });

    return { headers: headers };
  }

  public createPlaylist(name: string, userId: number) {
    const regExp = /{name}/gi;
    const regExp2 = /{userId}/gi;
    let url = this.CREATE_PLAYLIST.replace(regExp, name);
    url = url.replace(regExp2, userId.toString());
    return this.http.post<Observable<Object>>(url, {}, this.getOptions());
  }

  public getPlaylist(id: number) {
    const regExp = /{id}/gi;
    const url = this.GET_PLAYLIST.replace(regExp, id.toString());
    return this.http.get<Observable<Object>>(url, this.getOptions());
  }

  public deletePlaylist(id: number) {
    const regExp = /{id}/gi;
    const url = this.DELETE_PLAYLIST.replace(regExp, id.toString());
    return this.http.delete<Observable<Object>>(url, this.getOptions());
  }

  public getAllPlaylists() {
    return this.http.get<Observable<Object>>(this.GET_ALL_PLAYLISTS_WITH_TRACKS, this.getOptions());
  }

  public addTrackInPlaylist(playlistId: number, trackId: number) {
    const regExp = /{id}/gi;
    const regExp2 = /{trackId}/gi;
    let url = this.ADD_TRACK_IN_PLAYLIST.replace(regExp, playlistId.toString());
    url = url.replace(regExp2, trackId.toString());
    return this.http.put<Observable<Object>>(url, {}, this.getOptions());
  }

  public removeTrackFromPlaylist(playlistId: number, trackId: number) {
    const regExp = /{id}/gi;
    const regExp2 = /{trackId}/gi;
    let url = this.REMOVE_TRACK_FROM_PLAYLIST.replace(regExp, playlistId.toString());
    url = url.replace(regExp2, trackId.toString());
    return this.http.delete<Observable<Object>>(url, this.getOptions());
  }
}
