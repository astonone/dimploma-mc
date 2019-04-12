import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { HttpClient, HttpEvent, HttpHeaders, HttpRequest } from '@angular/common/http';
import { SharedService } from './shared.service';

@Injectable({
    providedIn: 'root'
})
export class FileService {
    HOST: string = 'http://localhost';
    PORT: string = '8082';
    SERVER_URL: string;

    USER_UPLOAD_PHOTO: string;
    GET_UPLOADED_PHOTO: string;
    UPLOAD_TRACK: string;
    GET_UPLOADED_TRACK: string;

    constructor(private http : HttpClient, private shared: SharedService) {
        this.SERVER_URL = this.shared.getServerURL();

        this.USER_UPLOAD_PHOTO = this.SERVER_URL + '/api/user/{id}/upload';
        this.GET_UPLOADED_PHOTO  = this.SERVER_URL + '/api/user/get/{filename}';
        this.UPLOAD_TRACK = this.SERVER_URL + '/api/track/files/upload';
        this.GET_UPLOADED_TRACK = this.SERVER_URL + '/api/track/get/{filename}';
    }

    getStorage() {
        if (localStorage.getItem('isRemember') === 'true') {
            return localStorage;
        } else {
            return sessionStorage;
        }
    }

    private getOptions() {
        let headers: HttpHeaders = new HttpHeaders({
            'Authorization': 'Basic ' + this.getStorage().getItem('token')
        });

        return { headers: headers };
    }

    pushPhotoFileToStorage(id:number, file: File) : Observable<HttpEvent<{}>> {
        let regExp = /{id}/gi;
        let url = this.USER_UPLOAD_PHOTO.replace(regExp, id + "");

        const formData: FormData = new FormData();

        formData.append('uploadedFile', file);

        const req = new HttpRequest('POST', url, formData, {
            headers: this.getOptions().headers,
            reportProgress: true,
            responseType: 'text'
        });

        return this.http.request(req);
    }

    pushAudioFileToStorage(file: File) : Observable<HttpEvent<{}>> {
        const formData: FormData = new FormData();

        formData.append('uploadedFile', file);

        const req = new HttpRequest('POST', this.UPLOAD_TRACK, formData, {
            headers: this.getOptions().headers,
            reportProgress: true,
            responseType: 'text'
        });

        return this.http.request(req);
    }

    getUploadedPhoto(filename: string) : Observable<any> {
        let regExp = /{filename}/gi;
        let url = this.GET_UPLOADED_PHOTO.replace(regExp, filename);
        return this.http.get(url, this.getOptions());
    }

    getUploadedTrack(filename: string) : Observable<any> {
        let regExp = /{filename}/gi;
        let url = this.GET_UPLOADED_TRACK.replace(regExp, filename);
        return this.http.get(url, this.getOptions());
    }
}
