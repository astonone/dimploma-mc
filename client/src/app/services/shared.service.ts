import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from './user.service';
import { User } from '../dto/user';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  isLogin : boolean;
  loggedUser : any;

  constructor(private router: Router,
              private userService: UserService) {}

  logout() {
    this.isLogin = false;
    this.loggedUser = {};
    sessionStorage.setItem('token', '');
    sessionStorage.setItem('loggedUser', '');
    this.router.navigate(['/login']);
  }

  setLoggedUser() {
    if (sessionStorage.getItem('loggedUser') !== null && sessionStorage.getItem('loggedUser') !== '') {
      this.loggedUser = new User(JSON.parse(sessionStorage.getItem('loggedUser')));
      this.isLogin = this.loggedUser !== null;
    }
  }

  getLoggedUser() {
    if (sessionStorage.getItem('loggedUser') !== null && sessionStorage.getItem('loggedUser') !== '') {
      return new User(JSON.parse(sessionStorage.getItem('loggedUser')));
    } else {
      return null;
    }
  }

  updateLoggedUser(user: User) {
    sessionStorage.setItem('loggedUser', JSON.stringify(user.toObject()))
  }

  createEmptyUserStub() {
    return new User({
      email: '',
      password: '',
      newPassword: null,
      dateCreate: {
        year: '',
        month: '',
        day: ''
      },
      userDetails: {
        firstName: '',
        lastName: '',
        about: '',
        birthday: {
          year: '',
          month: '',
          day: ''
        },
        nick: '',
        photoLink: ''
      }
    });
  }
}
