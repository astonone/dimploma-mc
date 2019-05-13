import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { SharedService } from './shared.service';

@Injectable({
    providedIn: 'root'
})
export class DialogService {
    private SERVER_URL: string;

    private CREATE_DIALOG: string;
    private GET_DIALOG: string;
    private DELETE_DIALOG: string;
    private GET_DIALOG_BY_USER: string;
    private ADD_USER_TO_DIALOG: string;
    private ADD_MESSAGE_TO_DIALOG: string;
    private START_DIALOG: string;

    constructor(private http: HttpClient, private shared: SharedService) {
        this.SERVER_URL = this.shared.getServerURL();

        this.CREATE_DIALOG = this.SERVER_URL + '/api/dialog/create?name={name}&user={userId}';
        this.GET_DIALOG = this.SERVER_URL + '/api/dialog/{id}';
        this.DELETE_DIALOG = this.SERVER_URL + '/api/dialog/{id}';
        this.GET_DIALOG_BY_USER = this.SERVER_URL + '/api/dialog/find/{id}';
        this.ADD_USER_TO_DIALOG = this.SERVER_URL + '/api/dialog/addUser/{id}?userId={userId}';
        this.ADD_MESSAGE_TO_DIALOG = this.SERVER_URL + '/api/dialog/addMessage/{id}?userId={userId}&text={text}';
        this.START_DIALOG = this.SERVER_URL + '/api/dialog/start/?userOne={userOne}&userTwo={userTwo}';
    }

    private getOptions() {
        const headers: HttpHeaders = new HttpHeaders({
            'Authorization': 'Basic ' + this.shared.getStorage().getItem('token')
        });

        return { headers: headers };
    }

    public createDialog(name: string, userId: number) {
        const regExp = /{name}/gi;
        const regExp2 = /{userId}/gi;
        let url = this.CREATE_DIALOG.replace(regExp, name);
        url = url.replace(regExp2, userId.toString());
        return this.http.post<Observable<Object>>(url, {}, this.getOptions());
    }

    public deleteDialog(id: number) {
        const regExp = /{id}/gi;
        const url = this.DELETE_DIALOG.replace(regExp, id.toString());
        return this.http.delete<Observable<Object>>(url, this.getOptions());
    }

    public getDialog(id: number) {
        const regExp = /{id}/gi;
        const url = this.GET_DIALOG.replace(regExp, id.toString());
        return this.http.get<Observable<Object>>(url, this.getOptions());
    }

    public getUserDialogs(userId: number) {
        const regExp = /{id}/gi;
        const url = this.GET_DIALOG_BY_USER.replace(regExp, userId.toString());
        return this.http.get<Observable<Object>>(url, this.getOptions());
    }

    public addUserToDialog(id: number, userId: number) {
        const regExp = /{id}/gi;
        const regExp2 = /{userId}/gi;
        let url = this.ADD_USER_TO_DIALOG.replace(regExp, id.toString());
        url = url.replace(regExp2, userId.toString());
        return this.http.post<Observable<Object>>(url, {}, this.getOptions());
    }

    public addMessageToDialog(id: number, userId: number, text: string) {
        const regExp = /{id}/gi;
        const regExp2 = /{userId}/gi;
        const regExp3 = /{text}/gi;
        let url = this.ADD_MESSAGE_TO_DIALOG.replace(regExp, id.toString());
        url = url.replace(regExp2, userId.toString());
        url = url.replace(regExp3, text);
        return this.http.post<Observable<Object>>(url, {}, this.getOptions());
    }

    public startDialog(id1: number, id2: number) {
        const regExp = /{userOne}/gi;
        const regExp2 = /{userTwo}/gi;
        let url = this.START_DIALOG.replace(regExp, id1.toString());
        url = url.replace(regExp2, id2.toString());
        return this.http.post<Observable<Object>>(url, {}, this.getOptions());
    }
}
