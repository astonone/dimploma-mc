import { Component, OnInit } from '@angular/core';
import { TrackService } from '../../services/track.service';

@Component({
  selector: 'app-music',
  templateUrl: './music.component.html',
  styleUrls: ['./music.component.css']
})
export class MusicComponent implements OnInit {

  tracks: any ;
  response: any;

  ngOnInit() {
    this.trackService.getAllTracks()
        .subscribe(data => {
          this.response = data;
          this.tracks = this.response.tracks;
        });
  }

  constructor(private trackService: TrackService) {
    this.tracks = [];
  }

  loadTracks() {

  }
}
