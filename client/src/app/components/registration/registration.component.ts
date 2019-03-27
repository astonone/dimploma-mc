import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { MatDialog } from '@angular/material';
import { CreateUserDialog } from './dialog/create-user-dialog';
import { ErrorCreateUserDialog } from './dialog/error-create-user-dialog';
import { SharedService } from '../../services/shared.service';
import { User } from '../../dto/user';
import { LocalDate } from '../../dto/local-date';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {

  newUser : any;
  createdUser : any;
  isNotValid : boolean;

  constructor(private userService : UserService,
              private shared : SharedService,
              public dialog: MatDialog) {
    this.newUser = this.createEmptyUser();
  }

  createUser() {
    if (this.isValidInput()) {
      this.userService.createUser(this.newUser.email, this.newUser.password)
          .subscribe(data => {
            this.createdUser = new User(data);
            let birthday = null;
            if (this.newUser.birthday !== '') {
              birthday = LocalDate.getObjFromDate(new Date(this.newUser.birthday));
            }
            let request = {
              firstName: this.newUser.firstName,
              lastName: this.newUser.lastName,
              nick: this.newUser.nick,
              birthday: birthday
            };
            this.userService.addUserDetails(this.createdUser.id, request)
                .subscribe(userDetails => {
                  this.openUserCreatedDialog(userDetails);
                }, error => {
                  this.openErrorUserCreatedDialog(error);
                  this.userService.deleteUser(this.createdUser.id);
                });
          }, error => {
            this.openErrorUserCreatedDialog(error);
          });
    } else {
      this.isNotValid = true;
    }
  }

  isValidInput() {
    return !(this.newUser.email === '') || !(this.newUser.password === '');
  }

  openUserCreatedDialog(userDetails : any) : void {
    const dialogRef = this.dialog.open(CreateUserDialog, {
        width: '350px',
        data: userDetails
      });
    dialogRef.afterClosed().subscribe(result => {
      this.newUser = this.createEmptyUser();
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

  private createEmptyUser() {
    return {
      email : "",
      password : "",
      firstName : "",
      lastName : "",
      nick : "",
      birthday : ""
    };
  }
}
