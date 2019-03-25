import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UserService} from '../../services/user.service';
import {Router, ActivatedRoute} from '@angular/router';
import { SharedService } from '../../services/shared.service';

@Component({
    selector: 'home',
    templateUrl: './home.component.html',
    styleUrls: [
        './home.component.css'
    ]
})

export class HomeComponent implements OnInit {

    user: any;
    userName: string;
    userNickName: string;
    userEmail: string;
    userBirthday: string;
    userAbout: string;
    myMusic: any;
    myFriends: any;
    myRequest: any;

    constructor(private http: HttpClient,
                private router: Router,
                private userService: UserService,
                private shared: SharedService) {
    }

    ngOnInit() {
        if (sessionStorage.getItem('token') !== '') {
            this.userService.auth()
                .subscribe(principal => {
                        let email = principal['name'];
                        this.userService.getUserByEmail(email)
                            .subscribe(data => {
                                this.user = data;
                                this.shared.isLogin = true;
                                this.shared.loggedUser = data;
                                this.setUserData();
                            });
                    },
                    error => {
                        if (error.status == 401) {
                            this.shared.logout();
                        }
                    }
                );
        }
    }

    setUserData() {
        this.userAbout = this.user.userDetails.about;
        this.userName = this.user.userDetails.firstName + " " + this.user.userDetails.lastName;
        this.userNickName = this.user.userDetails.nick;
        this.userEmail = this.user.email;
        this.userBirthday = this.user.userDetails.birthday.day + "." + this.user.userDetails.birthday.month + "." + this.user.userDetails.birthday.year;
        this.myMusic = [];
        this.myFriends = [];
        this.myRequest = [];
    }
}
