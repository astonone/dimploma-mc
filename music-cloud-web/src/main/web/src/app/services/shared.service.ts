import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../dto/user';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  isLogin : boolean;
  loggedUser : any;

  constructor(private router: Router) {}

  getStorage() {
    if (localStorage.getItem('isRemember') === 'true') {
      return localStorage;
    } else {
      return sessionStorage;
    }
  }

  logout() {
    this.isLogin = false;
    this.loggedUser = {};
    this.getStorage().setItem('token', '');
    this.getStorage().setItem('loggedUser', '');
    this.router.navigate(['/login']);
  }

  setLoggedUser() {
    if (this.getStorage().getItem('loggedUser') !== null && this.getStorage().getItem('loggedUser') !== '') {
      this.loggedUser = new User(JSON.parse(this.getStorage().getItem('loggedUser')));
      this.isLogin = this.loggedUser !== null;
    }
  }

  getLoggedUser() {
    if (this.getStorage().getItem('loggedUser') !== null && this.getStorage().getItem('loggedUser') !== '') {
      return new User(JSON.parse(this.getStorage().getItem('loggedUser')));
    } else {
      return null;
    }
  }

  updateLoggedUser(user: User) {
    this.getStorage().setItem('loggedUser', JSON.stringify(user.toObject()))
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
