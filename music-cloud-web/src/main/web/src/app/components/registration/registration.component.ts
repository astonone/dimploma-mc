import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { MatDialog } from '@angular/material';
import { CreateUserDialog } from './dialog/create-user-dialog';
import { ErrorCreateUserDialog } from './dialog/error-create-user-dialog';
import { User } from '../../dto/user';
import { LocalDate } from '../../dto/local-date';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {

  public newUser: any;
  private createdUser: any;
  public isNotValid: boolean;

  constructor(private userService: UserService,
              public dialog: MatDialog) {
    this.newUser = this.createEmptyUser();
  }

  public createUser() {
    if (this.isValidInput()) {
      this.userService.createUser(this.newUser.email, this.newUser.password)
          .subscribe(data => {
            this.createdUser = new User(data);
            let birthday = null;
            if (this.newUser.birthday !== '') {
              birthday = LocalDate.getObjFromDate(new Date(this.newUser.birthday));
            }
            const request = {
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

  private isValidInput() {
    return !(this.newUser.email === '') || !(this.newUser.password === '');
  }

  private openUserCreatedDialog(userDetails: any): void {
    const dialogRef = this.dialog.open(CreateUserDialog, {
        data: userDetails
      });
    dialogRef.afterClosed().subscribe(result => {
      this.newUser = this.createEmptyUser();
    });
  }

  private openErrorUserCreatedDialog(response: any): void {
    const dialogRef = this.dialog.open(ErrorCreateUserDialog, {
      data : response
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  }

  private createEmptyUser() {
    return {
      email : '',
      password : '',
      firstName : '',
      lastName : '',
      nick : '',
      birthday : ''
    };
  }
}
