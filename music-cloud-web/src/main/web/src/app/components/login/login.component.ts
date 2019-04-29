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

    public model: any = {};
    public isAuthError = false;
    public isRemember = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private userService: UserService,
        private shared: SharedService
    ) {
    }

    ngOnInit() {
        if (this.shared.getStorage().getItem('token') !== null && this.shared.getStorage().getItem('token') !== '') {
            this.router.navigate(['home']);
        }
    }

    public login() {
        if (this.isRemember) {
            localStorage.setItem('isRemember', 'true');
        } else {
            localStorage.setItem('isRemember', 'false');
        }
        const email = this.model.email;
        const password = this.model.password;
        this.userService.login(email, password)
            .subscribe(isValid => {
                if (isValid) {
                    this.isAuthError = false;
                    this.shared.getStorage().setItem('token', btoa(email + ':' + password));
                    this.shared.getStorage().setItem('loggedUser', '');
                    this.router.navigate(['home']);
                } else {
                    this.isAuthError = true;
                }
            });
    }
}
