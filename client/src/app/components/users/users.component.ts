import { Component, OnInit } from '@angular/core';
import { SharedService } from '../../services/shared.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  response : any;
  users : any = [];
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
        this.response = data;
        this.usersLength = this.response.allCount;
        this.users = this.response.users;
      });
    } else {
      this.userService.getAllUsers(this.page, this.pageSize).subscribe(data => {
        this.response = data;
        this.users = this.response.users;
      });
    }
  }
}
