import { Component, OnInit } from '@angular/core';
import { SharedService } from '../../services/shared.service';
import { UserService } from '../../services/user.service';
import { User } from '../../dto/user';
import { LocalDate } from '../../dto/local-date';
import { Observable } from 'rxjs';
import { FileService } from '../../services/file.service';
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  loggedUser : User;
  birthday : Date;
  isAccountDataNotCorrect : boolean;
  isAccountInfoDataNotCorrect : boolean;
  uploadedFile : any;
  isEmpty : boolean;
  isError : boolean;
  isSuccess : boolean;
  isSuccessAccountSaving : boolean;
  photos : Observable<string[]>;

  selectedFiles: FileList;
  currentFileUpload: File;
  progress: { percentage: number } = { percentage: 0 };

  constructor(private shared : SharedService,
              private userService : UserService,
              private fileService : FileService,
              private router: Router) {
      this.isAccountDataNotCorrect = false;
      this.isAccountInfoDataNotCorrect = false;
      this.loggedUser = this.shared.getLoggedUser();
      this.getPhoto();
      this.birthday = this.loggedUser.userDetails.birthday.toDate();
  }

   ngOnInit() {
       if (this.shared.getLoggedUser() === null) {
           this.router.navigate(['login']);
       }
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
                  this.getPhoto();
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
             this.shared.getStorage().setItem('token', btoa(email + ':' + password));
             this.shared.updateLoggedUser(this.loggedUser);
             this.getPhoto();
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

  selectFile(event) {
     this.selectedFiles = event.target.files;
     this.isError = false;
     this.isSuccess = false;
  }

  upload() {
     this.progress.percentage = 0;

     this.currentFileUpload = this.selectedFiles.item(0);
     this.fileService.pushPhotoFileToStorage(this.loggedUser.id, this.currentFileUpload)
         .subscribe(event => {
         if (event.type === HttpEventType.UploadProgress) {
             this.progress.percentage = Math.round(100 * event.loaded / event.total);
         } else if (event instanceof HttpResponse) {
             this.userService.getById(this.loggedUser.id + "")
                 .subscribe(data => {
                     this.loggedUser = new User(data);
                     this.shared.updateLoggedUser(this.loggedUser);
                     this.getPhoto();
                     this.isError = false;
                     this.isEmpty = false;
                 });
         }
        },error => {
             this.isError = true;
             this.isEmpty = false;
         });

     this.selectedFiles = undefined;
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

  getPhoto() {
     this.photos = this.fileService.getUploadedPhoto(this.loggedUser.getPhotoLink());
  }
}
