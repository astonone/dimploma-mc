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
import { InfoDialog } from '../home/dialog/info-dialog';
import { MatDialog } from '@angular/material';
import { UserList } from '../../dto/user-list';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  public user: User = User.createEmptyUser();
  public loggedUser: User;
  public photos: Observable<string[]>;
  public music: Track[] = [];
  private response: TrackList;
  public tracksLength = 10;
  public pageEvent: any;
  public page = 0;
  public pageSize = 10;
  public pageSizeOptions: any = [10, 25, 50, 10];
  public isFriend = false;
  public isRequest = false;
  public isRequestFromUser = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService,
              private fileService: FileService,
              private trackService: TrackService,
              private shared: SharedService,
              public dialog: MatDialog, ) { }

  ngOnInit() {
    if (this.shared.getLoggedUser() === null) {
      this.router.navigate(['login']);
    }
    const userId = this.route.snapshot.paramMap.get('id');
    this.loadUser(userId);
    this.loggedUser = this.shared.getLoggedUser();
    this.isAddFriendNonActive(userId);
    this.isRemoveFriendNonActive(userId);
    this.loadTracksList(null);
  }

  public isEmptyPhotoLink() {
    return this.user.email ? this.user.isEmptyPhotoLink() : false;
  }

  public printUserName() {
    return this.user.email ? this.user.printUserName() : '';
  }

  private loadUser(id: string) {
    this.userService.getById(id).subscribe(data => {
      this.user = new User(data);
      this.getPhoto();
    });
  }

  private getPhoto() {
    this.photos = this.fileService.getUploadedPhoto(this.user.getPhotoLink());
  }

  private loadTracksList(event) {
    const userId = Number(this.route.snapshot.paramMap.get('id'));
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

  private loadAudioFiles() {
    for (let i = 0; i < this.music.length; i++) {
      this.loadFile(this.music[i]);
    }
  }

  public loadFile(track: Track) {
    track.files = this.fileService.getUploadedTrack(track.filename);
  }

  public isAddFriendNonActive(userId: string) {
    this.userService.getAllFriends(parseInt(userId)).subscribe(data => {
        const response = new UserList(data);
        this.isFriend =  this.isFriendById(this.loggedUser.id, response.users);
        console.log('f:' + this.isFriend);
    });
  }

  public isFriendById(userId: number, users: User[]) {
    const index = users.map(x => {
      return x.id;
    }).indexOf(userId);

    return index !== -1;
  }

  public isRemoveFriendNonActive(userId: string) {
    this.userService.getAllFriendRequests(parseInt(userId)).subscribe(data => {
      const response = new UserList(data);
      this.isRequest = this.isFriendById(this.loggedUser.id, response.users);
      console.log('r:' + this.isRequest);
    });
    this.userService.getAllFriendRequests(this.loggedUser.id).subscribe(data => {
      const response = new UserList(data);
      this.isRequestFromUser = this.isFriendById(parseInt(userId), response.users);
      console.log('r:' + this.isRequestFromUser);
    });
  }

  public sendFriendRequest() {
    this.userService.sendFriendRequest(this.loggedUser.id, this.user.id)
        .subscribe(() => {
          this.openFriendDialog({title: 'Друзья', description: 'Заявка была отправлена'});
          this.isRequest = true;
        }, error => {
          this.openFriendDialog({title: 'Ошибка', description: 'Произошла ошибка:' + error.error.message});
        });
  }

  public removeFriend() {
    this.userService.removeFriend(this.loggedUser.id, this.user.id)
        .subscribe(() => {
          this.openFriendDialog({title: 'Друзья', description: 'Друг удален'});
          this.isFriend = false;
          this.isRequest = false;
        }, error => {
          this.openFriendDialog({title: 'Ошибка', description: 'Произошла ошибка:' + error.error.message});
        });
  }

  private openFriendDialog(data: any): void {
    const dialogRef = this.dialog.open(InfoDialog, {
      data : data
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  }
}
