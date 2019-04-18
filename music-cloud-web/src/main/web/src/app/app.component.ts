import { Component } from '@angular/core';
import { SharedService } from './services/shared.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: [
        './app.component.css'
    ]
})

export class AppComponent {

    constructor(
        public shared: SharedService) {
        this.shared.setLoggedUser();
    }

    showUserInfo() {
        const firstName = this.shared.loggedUser.userDetails.firstName === null ? '' : this.shared.loggedUser.userDetails.firstName;
        const lastName = this.shared.loggedUser.userDetails.lastName === null ? '' : this.shared.loggedUser.userDetails.lastName;
        return firstName === '' && lastName === '' ? this.shared.loggedUser.email : firstName + ' ' + lastName;
    }
}
