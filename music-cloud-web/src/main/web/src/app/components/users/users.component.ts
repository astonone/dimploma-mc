import { Component, OnInit } from '@angular/core';
import { SharedService } from '../../services/shared.service';
import { UserService } from '../../services/user.service';
import { UserList } from '../../dto/user-list';
import { User } from '../../dto/user';
import { Router } from '@angular/router';
import { FileService } from '../../services/file.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  response : UserList;
  users : User[] = [];
  usersLength : number = 10;
  pageEvent : any;
  page: number = 0;
  pageSize : number = 10;
  pageSizeOptions : any = [10,25,50];
  //filters
  firstName: string = '';
  lastName: string = '';
  nickName: string = '';
  isFind: boolean = false;

  constructor(private shared: SharedService,
              private userService: UserService,
              private router: Router,
              private fileService: FileService) { }

  ngOnInit() {
    if (this.shared.getLoggedUser() === null) {
      this.router.navigate(['login']);
    }
    this.loadUserList(null);
  }

  loadUserList(event) {
    if (event) {
      if (!this.isFind) {
        this.userService.getAllUsers(event.pageIndex, event.pageSize).subscribe(data => {
          this.response = new UserList(data);
          this.usersLength = this.response.allCount;
          this.users = this.response.users;
        });
      } else {
        this.userService.findUsers(event.pageIndex, event.pageSize, this.firstName, this.lastName, this.nickName).subscribe(data => {
          this.response = new UserList(data);
          this.usersLength = this.response.allCount;
          this.users = this.response.users;
        });
      }
    } else {
      if (!this.isFind) {
        this.userService.getAllUsers(this.page, this.pageSize).subscribe(data => {
          this.response = new UserList(data);
          this.usersLength = this.response.allCount;
          this.users = this.response.users;
        });
      } else {
        this.userService.findUsers(this.page, this.pageSize, this.firstName, this.lastName, this.nickName).subscribe(data => {
          this.response = new UserList(data);
          this.usersLength = this.response.allCount;
          this.users = this.response.users;
        });
      }
    }
  }

  gotoProfile(id: number) {
    this.router.navigate(['user/'+ id]);
  }

  showUserInfo(user: User) {
    const firstName = user.userDetails.firstName === null ? '' : user.userDetails.firstName;
    const lastName = user.userDetails.lastName === null ? '' : user.userDetails.lastName;
    return firstName === '' && lastName === '' ? user.email : firstName + ' ' + lastName;
  }

  findUsers() {
    this.isFind = true;
    this.users = [];
    this.usersLength = 0;
    this.loadUserList(null);
  }

  clearFilters() {
    this.isFind = false;
    this.loadUserList(null);
    this.firstName = '';
    this.lastName = '';
    this.nickName = '';
  }
}
