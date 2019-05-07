import { Component, OnInit } from '@angular/core';
import { SharedService } from '../../services/shared.service';
import { UserService } from '../../services/user.service';
import { UserList } from '../../dto/user-list';
import { User } from '../../dto/user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  private response: UserList;
  public users: User[] = [];
  public usersLength = 10;
  public pageEvent: any;
  public page = 0;
  public pageSize = 10;
  public pageSizeOptions: any = [10, 25, 50];
  // filters
  public firstName = '';
  public lastName = '';
  public nickName = '';
  public isFind = false;

  constructor(private shared: SharedService,
              private userService: UserService,
              private router: Router) { }

  ngOnInit() {
    if (this.shared.getLoggedUser() === null) {
      this.router.navigate(['music']);
    }
    this.loadUserList(null);
  }

  private loadUserList(event) {
    if (event) {
      if (!this.isFind) {
        this.userService.getAllUsers(event.pageIndex, event.pageSize).subscribe(data => {
          this.response = new UserList(data);
          this.usersLength = this.response.allCount;
          this.users = this.response.users;
          this.deleteMeFromList(this.shared.getLoggedUser().id, this.users);
        });
      } else {
        this.userService.findUsers(event.pageIndex, event.pageSize, this.firstName, this.lastName, this.nickName).subscribe(data => {
          this.response = new UserList(data);
          this.usersLength = this.response.allCount;
          this.users = this.response.users;
          this.deleteMeFromList(this.shared.getLoggedUser().id, this.users);
        });
      }
    } else {
      if (!this.isFind) {
        this.userService.getAllUsers(this.page, this.pageSize).subscribe(data => {
          this.response = new UserList(data);
          this.usersLength = this.response.allCount;
          this.users = this.response.users;
          this.deleteMeFromList(this.shared.getLoggedUser().id, this.users);
        });
      } else {
        this.userService.findUsers(this.page, this.pageSize, this.firstName, this.lastName, this.nickName).subscribe(data => {
          this.response = new UserList(data);
          this.usersLength = this.response.allCount;
          this.users = this.response.users;
          this.deleteMeFromList(this.shared.getLoggedUser().id, this.users);
        });
      }
    }
  }
  private deleteMeFromList(myId: number, users: User[]) {
    const index = users.map(x => {
      return x.id;
    }).indexOf(myId);

    users.splice(index, 1);
  }

  public gotoProfile(id: number) {
    this.router.navigate(['user/' + id]);
  }

  public showUserInfo(user: User) {
    const firstName = user.userDetails.firstName === null ? '' : user.userDetails.firstName;
    const lastName = user.userDetails.lastName === null ? '' : user.userDetails.lastName;
    return firstName === '' && lastName === '' ? user.email : firstName + ' ' + lastName;
  }

  public findUsers() {
    this.isFind = true;
    this.users = [];
    this.usersLength = 0;
    this.loadUserList(null);
  }

  public clearFilters() {
    this.isFind = false;
    this.loadUserList(null);
    this.firstName = '';
    this.lastName = '';
    this.nickName = '';
  }
}
