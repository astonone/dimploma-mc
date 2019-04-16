import { Component, OnInit } from '@angular/core';
import { User } from '../../dto/user';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { FileService } from '../../services/file.service';
import { Observable } from 'rxjs';
import {Track} from '../../dto/track';
import {TrackList} from '../../dto/track-list';
import {TrackService} from '../../services/track.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  user: User;
  photos: Observable<string[]>;
  music: Track[] = [];
  response: TrackList;
  tracksLength : number = 10;
  pageEvent : any;
  page: number = 0;
  pageSize : number = 10;
  pageSizeOptions : any = [10,25,50,10];

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService,
              private fileService: FileService,
              private trackService: TrackService) { }

  ngOnInit() {
    let userId = this.route.snapshot.paramMap.get('id');
    this.loadUser(userId);
    this.loadTracksList(null);
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
}
