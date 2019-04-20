import { Component, OnInit } from '@angular/core';
import { SharedService } from '../../services/shared.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {

  constructor(private shared: SharedService,
              private router: Router) { }

  ngOnInit() {
    if (this.shared.getLoggedUser() === null) {
      this.router.navigate(['login']);
    }
  }
}
