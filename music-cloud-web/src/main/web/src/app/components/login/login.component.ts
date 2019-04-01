import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/user.service';
import { SharedService } from '../../services/shared.service';

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
    isRemember: boolean = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private userService: UserService,
        private shared: SharedService
    ) {
    }

    ngOnInit() {
        if (this.shared.getStogare().getItem('token') !== null && this.shared.getStogare().getItem('token') !== '') {
            this.router.navigate(['home']);
        }
    }

    login() {
        if (this.isRemember) {
            localStorage.setItem('isRemember', 'true');
        } else {
            localStorage.setItem('isRemember', 'false');
        }
        let email = this.model.email;
        let password = this.model.password;
        this.userService.login(email, password)
            .subscribe(isValid => {
                if (isValid) {
                    this.isAuthError = false;
                    this.shared.getStogare().setItem('token', btoa(email + ':' + password));
                    this.shared.getStogare().setItem('loggedUser', '');
                    this.router.navigate(['home']);
                } else {
                    this.isAuthError = true;
                }
            });
    }
}
