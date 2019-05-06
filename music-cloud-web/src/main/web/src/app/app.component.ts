import { Component } from '@angular/core';
import { SharedService } from './services/shared.service';
import {PlayService} from './services/play.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: [
        './app.component.css'
    ]
})

export class AppComponent {
    constructor(public shared: SharedService,
                private playService: PlayService) {
        this.shared.setLoggedUser();
    }

    public showUserInfo() {
        const firstName = this.shared.getLoggedUser().userDetails.firstName === null ? '' :
            this.shared.getLoggedUser().userDetails.firstName;
        const lastName = this.shared.getLoggedUser().userDetails.lastName === null ? '' :
            this.shared.getLoggedUser().userDetails.lastName;
        return firstName === '' && lastName === '' ? this.shared.getLoggedUser().email
            : firstName + ' ' + lastName;
    }

    public logout() {
        if (this.playService.isPlaying()) {
            this.playService.stopPlayingAndClearData();
        }
        this.shared.logout();
    }

    toggleSidenav(sidenav: any) {
        if (this.shared.isMobile()) {
            sidenav.toggle();
        }
    }
}
