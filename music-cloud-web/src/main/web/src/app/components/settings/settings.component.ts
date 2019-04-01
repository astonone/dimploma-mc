import { Component } from '@angular/core';
import { SharedService } from '../../services/shared.service';
import { UserService } from '../../services/user.service';
import { User } from '../../dto/user';
import {LocalDate} from '../../dto/local-date';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent {

  loggedUser : User;
  birthday : Date;
  isAccountDataNotCorrect : boolean;
  isAccountInfoDataNotCorrect : boolean;
  uploadedFile : any;
  isNotChoosed : boolean;
  isEmpty : boolean;
  isError : boolean;
  isSuccess : boolean;
  isSuccessAccountSaving : boolean;

  constructor(private shared : SharedService,
              private userService : UserService) {
      this.isAccountDataNotCorrect = false;
      this.isAccountInfoDataNotCorrect = false;
      this.loggedUser = this.shared.getLoggedUser();
      this.birthday = this.loggedUser.userDetails.birthday.toDate();
  }

  saveUserInfo() {
      if (this.validUserInfo()) {
          if (this.birthday !== null) {
              this.loggedUser.userDetails.birthday = LocalDate.fromDate(this.birthday);
          }
          this.userService.updateUserInfo(this.loggedUser)
              .subscribe(data => {
                  this.isSuccess = true;
                  this.isAccountInfoDataNotCorrect = false;
                  this.loggedUser = new User(data);
                  this.shared.updateLoggedUser(this.loggedUser);
                  if (this.birthday !== null) {
                    this.birthday = this.loggedUser.userDetails.birthday.toDate();
                  }
              }, error => {
                  this.isAccountInfoDataNotCorrect = true;
                  this.isSuccess = false;
              });
      } else {
          this.isAccountInfoDataNotCorrect = true;
          this.isSuccess = false;
      }
  }

  validUserInfo() {
      return !(this.loggedUser.userDetails.firstName === '') || !(this.loggedUser.userDetails.lastName === '') ||
          !(this.loggedUser.userDetails.nick === '') || !(this.loggedUser.userDetails.about === '') ||
          !(this.birthday !== null);
  }

  saveAccount() {
      let email = this.loggedUser.email;
      let password = this.loggedUser.newPassword;
     this.userService.updateUser(this.loggedUser.toObject())
         .subscribe(data => {
             this.isAccountDataNotCorrect = false;
             this.isSuccessAccountSaving = true;
             this.loggedUser = new User(data);
             this.shared.getStogare().setItem('token', btoa(email + ':' + password));
             this.shared.updateLoggedUser(this.loggedUser);
         },error => {
             this.isAccountDataNotCorrect = true;
             this.isSuccessAccountSaving = true;
         });
  }

  deleteAccount() {
      this.userService.deleteUser(this.loggedUser.id)
          .subscribe(() => {
             this.shared.logout();
          });
  }

  setFileForUpload(files: any) {
      let fd = new FormData();
      fd.append("uploadedFile", files[0]);
      this.uploadedFile = fd;
      this.isNotChoosed = false;
      this.isError = false;
      this.isSuccess = false;
  }

  uploadFile() {
      if (!this.isNotChoosed) {
          this.userService.uploadPhoto(this.loggedUser.id, this.uploadedFile)
              .subscribe(data => {
                  this.loggedUser = new User(data);
                  this.shared.updateLoggedUser(this.loggedUser);
                  this.isError = false;
                  this.isEmpty = false;
               },error => {
                  this.isError = true;
                  this.isEmpty = false;
               });
      }
  }

  deleteFile() {
     if (!this.loggedUser.isEmptyPhotoLink()) {
         this.userService.deletePhoto(this.loggedUser.id)
             .subscribe(data => {
                 this.isEmpty = false;
                 this.isSuccess = false;
                 this.loggedUser = new User(data);
                 this.shared.updateLoggedUser(this.loggedUser);
             });
     } else {
         this.isEmpty = true;
         this.isSuccess = false;
     }
  }
}
