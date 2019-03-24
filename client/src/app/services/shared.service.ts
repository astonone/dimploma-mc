import { Injectable } from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  isLogin : boolean;
  loggedUser : any;

  constructor(private router: Router) {}

  logout() {
    this.isLogin = false;
    this.loggedUser = {};
    sessionStorage.setItem('token', '');
    this.router.navigate(['/login']);
  }
}
