import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../dto/user';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  private isProd: boolean = environment.production;

  private HOST_DEV = 'http://localhost';
  private PORT_DEV = '8082';

  private HOST_PROD = 'https://music-cloud-social.herokuapp.com';

  private isLogin: boolean;
  private loggedUser: any;

  constructor(private router: Router) {}

  public isMobile = () => screen.width < 481;

  public getServerURL() {
    return this.isProd ? (this.HOST_PROD) : (this.HOST_DEV + ':' + this.PORT_DEV);
  }

  public getStorage = () =>  localStorage.getItem('isRemember') === 'true' ? localStorage : sessionStorage;

  public isLoginPage = () => this.router.url === '/login';

  public logout() {
    this.isLogin = false;
    this.loggedUser = {};
    this.getStorage().setItem('token', '');
    this.getStorage().setItem('loggedUser', '');
    this.router.navigate(['/login']);
  }

  public setLoggedUser() {
    if (this.getStorage().getItem('loggedUser') !== null && this.getStorage().getItem('loggedUser') !== '') {
      this.loggedUser = new User(JSON.parse(this.getStorage().getItem('loggedUser')));
      this.isLogin = this.loggedUser !== null;
    }
  }

  public getLoggedUser = () => (this.getStorage().getItem('loggedUser') !== null && this.getStorage().getItem('loggedUser') !== '') ?
      new User(JSON.parse(this.getStorage().getItem('loggedUser'))) : null;


  public isUserLogin = () => this.isLogin;

  public updateLoggedUser(user: User) {
    this.getStorage().setItem('loggedUser', JSON.stringify(user.toObject()));
  }

  public createEmptyUserStub() {
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
