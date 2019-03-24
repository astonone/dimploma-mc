import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { MatDialog } from '@angular/material';
import { CreateUserDialog } from './dialog/create-user-dialog';
import { ErrorCreateUserDialog } from './dialog/error-create-user-dialog';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  newUser : any;
  createdUser : any;
  isNotValid : boolean;

  constructor(private userService : UserService,
              public dialog: MatDialog) {
  }

  ngOnInit() {
    this.newUser = {
      email : "",
      password : "",
      firstName : "",
      lastName : "",
      nick : "",
      birthday : ""
    }
  }

  createUser() {
    if (this.isValidInput()) {
      let email = this.newUser.email;
      let password = this.newUser.password;
      this.userService.createUser(email, password)
          .subscribe(user => {
            this.createdUser = user;
            let newUserId = this.createdUser.id;
            let birthday = new Date(this.newUser.birthday);
            let requset = {
              firstName: this.newUser.firstName,
              lastName: this.newUser.lastName,
              nick: this.newUser.nick,
              birthday: {
                year: birthday.getFullYear(),
                month: birthday.getMonth() + 1,
                day: birthday.getDay(),
                hours: '0',
                minutes: '0',
                seconds: '0'
              }
            };
            this.userService.addUserDetails(newUserId, requset)
                .subscribe(userDetails => {
                  this.openUserCreatedDialog(userDetails);
                }, error => {
                  this.openErrorUserCreatedDialog(error);
                });
          }, error => {
            this.openErrorUserCreatedDialog(error);
          });
    } else {
      this.isNotValid = true;
    }
  }

  isValidInput() {
    return !this.newUser.email.includes("") || !this.newUser.password.includes("") || !this.newUser.firstName.includes("") ||
        !this.newUser.lastName.includes("") || !this.newUser.nick.includes("") || !this.newUser.birthday.includes("");
  }

  openUserCreatedDialog(userDetails : any) : void {
    const dialogRef = this.dialog.open(CreateUserDialog, {
        width: '350px',
        data: userDetails
      });
    dialogRef.afterClosed().subscribe(result => {
      this.newUser = {
        email : "",
        password : "",
        firstName : "",
        lastName : "",
        nick : "",
        birthday : ""
      }
    });
  }

  openErrorUserCreatedDialog(response : any) : void{
    const dialogRef = this.dialog.open(ErrorCreateUserDialog, {
      width: '250px',
      data : response
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  }
}
