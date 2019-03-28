import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { SharedService } from '../../services/shared.service';
import { User } from '../../dto/user';

@Component({
    selector: 'home',
    templateUrl: './home.component.html',
    styleUrls: [
        './home.component.css'
    ]
})

export class HomeComponent implements OnInit {
    user: User;
    myMusic: any = [];
    myFriends: any = [];
    myRequest: any = [];

    constructor(private http: HttpClient,
                private router: Router,
                private userService: UserService,
                private shared: SharedService) {
        this.user = this.shared.createEmptyUserStub();
    }

    ngOnInit() {
        if (this.shared.getStogare().getItem('token') !== null && this.shared.getStogare().getItem('token')) {
        if (this.shared.getStogare().getItem('loggedUser') === '') {
            this.userService.auth()
                .subscribe(data => {
                        this.shared.getStogare().setItem('loggedUser', JSON.stringify(data));
                        this.shared.setLoggedUser();
                        this.user = new User(data);
                    },
                    error => {
                        if (error.status == 401) {
                            this.shared.logout();
                        }
                    }
                );
        } else {
            this.user = new User(JSON.parse(this.shared.getStogare().getItem('loggedUser')));
        }
        } else {
            this.router.navigate(['login']);
        }
    }

    isEmptyPhotoLink() {
        return this.user.email !== '' ? this.user.isEmptyPhotoLink() : false;
    }

    getUserPhotoLink() {
        return this.user.email !== '' ? this.user.getPhotoLink() : '';
    }

    printUserName() {
        return this.user.email !== '' ? this.user.printUserName() : '';
    }
}
