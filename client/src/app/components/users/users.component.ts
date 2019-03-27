import { Component, OnInit } from '@angular/core';
import { SharedService } from '../../services/shared.service';
import { UserService } from '../../services/user.service';
import { UserList } from '../../dto/user-list';
import { User } from '../../dto/user';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  response : UserList;
  users : User[] = [];
  usersLength : number = 100;
  page: number = 0;
  pageSize : number = 10;
  pageSizeOptions : any = [10,25,50,10];

  constructor(private shared: SharedService,
              private userService: UserService) { }

  ngOnInit() {
    this.loadUserList(null);
  }

  loadUserList(event) {
    if (event) {
      this.userService.getAllUsers(event.pageIndex, event.pageSize).subscribe(data => {
        this.response = new UserList(data);
        this.usersLength = this.response.allCount;
        this.users = this.response.users;
      });
    } else {
      this.userService.getAllUsers(this.page, this.pageSize).subscribe(data => {
        this.response = new UserList(data);
        this.users = this.response.users;
      });
    }
  }
}
