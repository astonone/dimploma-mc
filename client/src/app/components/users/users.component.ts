import { Component, OnInit } from '@angular/core';
import {SharedService} from '../../services/shared.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  constructor(private shared: SharedService) { }

  ngOnInit() {
  }

}
