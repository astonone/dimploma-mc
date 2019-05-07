import { Component } from '@angular/core';
import { SharedService } from './services/shared.service';
import { PlayService } from './services/play.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: [
        './app.component.css'
    ]
})

export class AppComponent {
    constructor(public shared: SharedService,
                private playService: PlayService,
                private router: Router) {
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

    public gotoLogin() {
        if (this.playService.isPlaying()) {
            this.playService.stopPlayingAndClearData();
        }
        this.router.navigate(['login']);
    }

    toggleSidenav(sidenav: any) {
        if (this.shared.isMobile()) {
            sidenav.toggle();
        }
    }
}
