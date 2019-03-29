import { Component, OnInit } from '@angular/core';
import { TrackService } from '../../services/track.service';
import { TrackList } from '../../dto/track-list';
import { Track } from '../../dto/track';
import { AddTrackToUserDialog } from './dialog/add-track-to-user-dialog';
import { MatDialog } from '@angular/material';
import { SharedService } from '../../services/shared.service';
import { User } from '../../dto/user';
import { DeleteTrackDialog } from './dialog/delete-track-dialog';

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
  page: number = 0;
  pageSize : number = 10;
  pageSizeOptions : any = [10,25,50,10];

  ngOnInit() {
    this.loadTracksList(null);
  }

  constructor(private trackService: TrackService,
              public dialog: MatDialog,
              private shared: SharedService) {
    this.user = this.shared.getLoggedUser();
  }

  loadTracksList(event) {
    if (event) {
      this.trackService.getAllTracks(event.pageIndex, event.pageSize)
          .subscribe(data => {
            this.response = new TrackList(data);
            this.tracksLength = this.response.allCount;
            this.tracks = this.response.tracks;
          });
    } else {
      this.trackService.getAllTracks(this.page, this.pageSize).subscribe(data => {
        this.response = new TrackList(data);
        this.tracks = this.response.tracks;
        this.tracksLength = this.response.allCount;
      });
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
      data : track
    });
    dialogRef.afterClosed().subscribe(result => {
      this.loadTracksList(null);
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
}
