import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SharedService } from './shared.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GenreService {
  private SERVER_URL: string;

  private CREATE_GENRE: string;
  private DELETE_GENRE: string;
  private GET_GENRE: string;
  private GET_GENRES: string;

  constructor(private http : HttpClient,
              private shared : SharedService) {
    this.SERVER_URL = this.shared.getServerURL();

    this.CREATE_GENRE = this.SERVER_URL + 'api/genre/create?name={name}';
    this.GET_GENRE = this.SERVER_URL + '/api/genre/{id}';
    this.DELETE_GENRE = this.SERVER_URL + '/api/genre/{id}';
    this.GET_GENRES = this.SERVER_URL + '/api/genre/findAll';
  }

  private getOptions() {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + this.shared.getStorage().getItem('token')
    });

    return { headers: headers };
  }

  getAllGenres() {
    return this.http.get<Observable<Object>>(this.GET_GENRES, this.getOptions())
  }
}
