import { Component, OnInit } from '@angular/core';
import { User } from '../../dto/user';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { FileService } from '../../services/file.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  user: User;
  music: any = [];
  photos: Observable<string[]>;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService,
              private fileService: FileService) { }

  ngOnInit() {
    let userId = this.route.snapshot.paramMap.get('id');
    this.loadUser(userId);
  }

  isEmptyPhotoLink() {
    return this.user.email !== '' ? this.user.isEmptyPhotoLink() : false;
  }

  printUserName() {
    return this.user.email !== '' ? this.user.printUserName() : '';
  }

  loadUser(id: string) {
    this.userService.getById(id).subscribe(data => {
      this.user = new User(data);
      this.getPhoto();
    })
  }

  getPhoto() {
    this.photos = this.fileService.getUploadedPhoto(this.user.getPhotoLink());
  }
}
