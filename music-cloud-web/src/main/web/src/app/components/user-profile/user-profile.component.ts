import { Component, OnInit } from '@angular/core';
import { User } from '../../dto/user';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { FileService } from '../../services/file.service';
import { Observable } from 'rxjs';
import { Track } from '../../dto/track';
import { TrackList } from '../../dto/track-list';
import { TrackService } from '../../services/track.service';
import { SharedService } from '../../services/shared.service';
import { FriendDialog } from '../home/dialog/friend-dialog';
import { MatDialog } from '@angular/material';
import { UserList } from '../../dto/user-list';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  user: User;
  loggedUser: User;
  photos: Observable<string[]>;
  music: Track[] = [];
  response: TrackList;
  tracksLength : number = 10;
  pageEvent : any;
  page: number = 0;
  pageSize : number = 10;
  pageSizeOptions : any = [10,25,50,10];
  isFriend : boolean = false;
  isRequest : boolean = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService,
              private fileService: FileService,
              private trackService: TrackService,
              private shared: SharedService,
              public dialog: MatDialog,) { }

  ngOnInit() {
    if (this.shared.getLoggedUser() === null) {
      this.router.navigate(['login']);
    }
    let userId = this.route.snapshot.paramMap.get('id');
    this.loadUser(userId);
    this.loggedUser = this.shared.getLoggedUser();
    this.isAddFriendNonActive(userId);
    this.isRemoveFriendNonActive(userId);
    this.loadTracksList(null);
  }

  isEmptyPhotoLink() {
    return this.user.email ? this.user.isEmptyPhotoLink() : false;
  }

  printUserName() {
    return this.user.email ? this.user.printUserName() : '';
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

  loadTracksList(event) {
    let userId = Number(this.route.snapshot.paramMap.get('id'));
    if (event) {
      this.trackService.getUserTracks(userId, event.pageIndex, event.pageSize)
          .subscribe(data => {
            this.response = new TrackList(data);
            this.tracksLength = this.response.allCount;
            this.music = this.response.tracks;
            this.loadAudioFiles();
          });
    } else {
      this.trackService.getUserTracks(userId, this.page, this.pageSize)
          .subscribe(data => {
            this.response = new TrackList(data);
            this.tracksLength = this.response.allCount;
            this.music = this.response.tracks;
            this.loadAudioFiles();
          });
    }
  }

  loadAudioFiles() {
    for (let i = 0; i < this.music.length; i++) {
      this.loadFile(this.music[i]);
    }
  }

  loadFile(track: Track) {
    track.files = this.fileService.getUploadedTrack(track.filename);
  }

  isAddFriendNonActive(userId: string) {
    this.userService.getAllFriends(parseInt(userId)).subscribe(data => {
        let response = new UserList(data);
        this.isFriend =  this.isFriendById(this.loggedUser.id, response.users)
    });
  }

  isFriendById(userId: number, users: User[]) {
    let index = users.map(x => {
      return x.id;
    }).indexOf(userId);

    return index !== -1;
  }

  isRemoveFriendNonActive(userId: string) {
    this.userService.getAllFriendRequests(parseInt(userId)).subscribe(data => {
      let response = new UserList(data);
      this.isRequest = this.isFriendById(this.loggedUser.id, response.users)
    });
  }

  sendFriendRequest() {
    this.userService.sendFriendRequest(this.loggedUser.id, this.user.id)
        .subscribe(() => {
          this.openFriendDialog({title:'Друзья', description: 'Заявка была отправлена'});
          this.isRequest = true;
        }, error => {
          this.openFriendDialog({title:'Ошибка', description: 'Произошла ошибка:' + error.error.message});
        });
  }

  removeFriend() {
    this.userService.removeFriend(this.loggedUser.id, this.user.id)
        .subscribe(() => {
          this.openFriendDialog({title:'Друзья', description: 'Друг удален'});
          this.isFriend = false;
          this.isRequest = false;
        }, error => {
          this.openFriendDialog({title:'Ошибка', description: 'Произошла ошибка:' + error.error.message});
        });
  }

  openFriendDialog(data : any) : void {
    const dialogRef = this.dialog.open(FriendDialog, {
      data : data
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  };
}
