import { Component, OnInit } from '@angular/core';
import { TrackService } from '../../services/track.service';
import { TrackList } from '../../dto/track-list';
import { Track } from '../../dto/track';
import { AddTrackToUserDialog } from './dialog/add-track-to-user-dialog';
import { MatDialog } from '@angular/material';
import { SharedService } from '../../services/shared.service';
import { User } from '../../dto/user';
import { DeleteTrackDialog } from './dialog/delete-track-dialog';
import { ChangeTrackDialog } from './dialog/change-track-dialog';
import { FileService } from '../../services/file.service';

@Component({
  selector: 'app-music',
  templateUrl: './music.component.html',
  styleUrls: ['./music.component.css']
})
export class MusicComponent implements OnInit {

  user: User;
  tracks: Track[] = [];
  response: TrackList;
  tracksLength : number = 10;
  pageEvent : any;
  page: number = 0;
  pageSize : number = 10;
  pageSizeOptions : any = [10,25,50,10];

  ngOnInit() {
    this.loadTracksList(null);
  }

  constructor(private trackService: TrackService,
              public dialog: MatDialog,
              private shared: SharedService,
              private fileService: FileService) {
    this.user = this.shared.getLoggedUser();
  }

  loadTracksList(event) {
    if (event) {
      this.trackService.getAllTracks(event.pageIndex, event.pageSize)
          .subscribe(data => {
            this.response = new TrackList(data);
            this.tracksLength = this.response.allCount;
            this.tracks = this.response.tracks;
            this.loadAudioFiles();
          });
    } else {
      this.trackService.getAllTracks(this.page, this.pageSize).subscribe(data => {
        this.response = new TrackList(data);
        this.tracks = this.response.tracks;
        this.tracksLength = this.response.allCount;
        this.loadAudioFiles();
      });
    }
  }

  loadAudioFiles() {
    for (let i = 0; i < this.tracks.length; i++) {
      this.loadFile(this.tracks[i]);
    }
  }

  addTrackToUser(id: number) {
    this.trackService.addTrackToUser(this.user.id, id)
        .subscribe(data => {
          this.openTrackCreatedDialog(null);
        });
  }

  deleteTrack(track : Track) {
    const dialogRef = this.dialog.open(DeleteTrackDialog, {
      width: '250px',
      data : null
    });
    dialogRef.afterClosed().subscribe(result => {
      this.trackService.deleteTrack(track.id).subscribe(() => {
        this.loadTracksList(null);
      });
    });
  }

  openTrackCreatedDialog(response : any) : void {
    const dialogRef = this.dialog.open(AddTrackToUserDialog, {
      width: '250px',
      data : response
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  };

  changeTrack(track : Track) {
    const dialogRef = this.dialog.open(ChangeTrackDialog, {
      width: '400px',
      data : track
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  }

  rateTrack(track : Track) {
    this.trackService.rateTrack(track.id, this.user.id, track.tempRating)
        .subscribe(data => {
          let updatedTrack = new Track(data);
          track.rating = updatedTrack.rating;
          track.tempRating = null;
        });
  }

  loadFile(track: Track) {
    track.files = this.fileService.getUploadedTrack(track.filename);
  }
}
