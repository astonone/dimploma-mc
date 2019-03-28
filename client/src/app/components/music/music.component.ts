import { Component, OnInit } from '@angular/core';
import { TrackService } from '../../services/track.service';
import { TrackList } from '../../dto/track-list';
import { Track } from '../../dto/track';

@Component({
  selector: 'app-music',
  templateUrl: './music.component.html',
  styleUrls: ['./music.component.css']
})
export class MusicComponent implements OnInit {

  tracks: Track[];
  response: TrackList;
  tracksLength : number = 100;
  page: number = 0;
  pageSize : number = 10;
  pageSizeOptions : any = [10,25,50,10];

  ngOnInit() {
    this.loadTracksList(null);
  }

  constructor(private trackService: TrackService) {
    this.tracks = [];
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
      });
    }
  }
}
