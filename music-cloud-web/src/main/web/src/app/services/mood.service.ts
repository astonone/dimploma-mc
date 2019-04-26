import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SharedService } from './shared.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MoodService {
  private SERVER_URL: string;

  private CREATE_MOOD: string;
  private DELETE_MOOD: string;
  private GET_MOOD: string;
  private GET_MOODS: string;

  constructor(private http : HttpClient,
              private shared : SharedService) {
    this.SERVER_URL = this.shared.getServerURL();

    this.CREATE_MOOD = this.SERVER_URL + 'api/mood/create?name={name}';
    this.GET_MOOD = this.SERVER_URL + '/api/mood/{id}';
    this.DELETE_MOOD = this.SERVER_URL + '/api/mood/{id}';
    this.GET_MOODS = this.SERVER_URL + '/api/mood/findAll';
  }

  private getOptions() {
    let headers: HttpHeaders = new HttpHeaders({
      'Authorization': 'Basic ' + this.shared.getStorage().getItem('token')
    });

    return { headers: headers };
  }

  getAllMoods() {
    return this.http.get<Observable<Object>>(this.GET_MOODS, this.getOptions())
  }
}
