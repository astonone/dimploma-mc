import { Component, OnInit } from '@angular/core';
import { SharedService } from '../../services/shared.service';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  loggedUser : any;
  response : any;
  isAccountDataNotCorrect : boolean;

  constructor(private sharedService : SharedService,
              private userService : UserService) { }

  ngOnInit() {
    this.isAccountDataNotCorrect = false;
    this.loggedUser = this.getLoggedUser();
  }

  getLoggedUser() {
    if (sessionStorage.getItem('token') !== '') {
      this.userService.auth()
          .subscribe(principal => {
                let email = principal['name'];
                this.userService.getUserByEmail(email)
                    .subscribe(data => {
                      this.response = data;
                      this.loggedUser = this.response;
                      this.loggedUser.newPassword = "";
                    });
              }
          );
    }
  }

    saveAccount() {
        this.userService.updateUser(this.loggedUser)
            .subscribe(data => {
                this.isAccountDataNotCorrect = false;
                this.loggedUser = data;
            }, error => {
                this.isAccountDataNotCorrect = true;
            });
    }

}
