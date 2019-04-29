import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TrackService } from '../../services/track.service';
import { GenreService } from '../../services/genre.service';
import { MoodService } from '../../services/mood.service';
import { Track } from '../../dto/track';

@Component({
  selector: 'app-additional',
  templateUrl: './additional.component.html',
  styleUrls: ['./additional.component.css']
})
export class AdditionalComponent implements OnInit {

  public track: Track;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private trackService: TrackService,
              private genreService: GenreService,
              private moodService: MoodService) {
    const trackId = this.route.snapshot.paramMap.get('id');
    this.loadTrack(trackId);
  }

  ngOnInit() {
  }

  private loadTrack(trackId: string) {
      this.trackService.getTrack(Number(trackId)).subscribe(data => {
          this.track = new Track(data);
      });
  }
}
