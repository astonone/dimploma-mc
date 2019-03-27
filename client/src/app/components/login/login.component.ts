﻿import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
    selector: 'login',
    templateUrl: './login.component.html',
    styleUrls: [
        './login.component.css'
    ]
})

export class LoginComponent implements OnInit {

    model: any = {};
    isAuthError: boolean = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private userService: UserService
    ) {
    }

    ngOnInit() {
        if (sessionStorage.getItem('token') !== null && sessionStorage.getItem('token') !== '') {
            this.router.navigate(['home']);
        }
    }

    login() {
        let email = this.model.email;
        let password = this.model.password;
        this.userService.login(email, password)
            .subscribe(isValid => {
                if (isValid) {
                    this.isAuthError = false;
                    sessionStorage.setItem('token', btoa(email + ':' + password));
                    sessionStorage.setItem('loggedUser', '');
                    this.router.navigate(['home']);
                } else {
                    this.isAuthError = true;
                }
            });
    }
}
