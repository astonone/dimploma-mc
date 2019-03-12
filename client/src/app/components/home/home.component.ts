import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UserService} from '../../services/user.service';
import {Router, ActivatedRoute} from '@angular/router';

@Component({
    selector: 'home',
    templateUrl: './home.component.html',
    styleUrls: [
        './home.component.css'
    ]
})

export class HomeComponent implements OnInit {

    user: any;

    constructor(private http: HttpClient,
                private router: Router,
                private userService: UserService) {
    }

    ngOnInit() {
        this.userService.auth()
            .subscribe(principal => {
                let email = principal['name'];
                this.userService.getUserByEmail(email)
                    .subscribe(data => {
                        this.user = data;
                    })
            },
            error => {
                if (error.status == 401)
                    alert('Unauthorized');
            }
        );
    }

    logout() {
        sessionStorage.setItem('token', '');
        this.router.navigate(['/login']);
    }
}