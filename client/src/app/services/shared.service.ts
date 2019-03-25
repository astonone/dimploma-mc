import {Injectable, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {UserService} from './user.service';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  isLogin : boolean;
  loggedUser : any;
  response : any;

  constructor(private router: Router,
              private userService: UserService) {}

  logout() {
    this.isLogin = false;
    this.loggedUser = {};
    sessionStorage.setItem('token', '');
    this.router.navigate(['/login']);
  }

  checkLoggedUser() {
    if (sessionStorage.getItem('token') !== '') {
      this.userService.auth()
          .subscribe(principal => {
                let email = principal['name'];
                this.userService.getUserByEmail(email)
                    .subscribe(data => {
                      this.response = data;
                      this.isLogin = true;
                      this.loggedUser = this.response;
                    });
              }
          );
    }
  }
}
