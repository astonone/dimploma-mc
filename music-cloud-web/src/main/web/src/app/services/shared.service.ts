import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../dto/user';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  isProd: boolean = environment.production;

  HOST_DEV: string = 'http://localhost';
  PORT_DEV: string = '8082';

  HOST_PROD: string = 'http://music-cloud-social.herokuapp.com';

  isLogin : boolean;
  loggedUser : any;

  constructor(private router: Router) {}

  getServerURL() {
    return this.isProd ? (this.HOST_PROD) : (this.HOST_DEV + ':' + this.PORT_DEV);
  }

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
