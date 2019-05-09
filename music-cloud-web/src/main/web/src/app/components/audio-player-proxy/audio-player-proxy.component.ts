import { Component, Input, OnInit } from '@angular/core';
import { Track } from '../../dto/track';
import { ChangeTrackDialog } from '../music/dialog/change-track-dialog';
import { SharedService } from '../../services/shared.service';
import { TrackService } from '../../services/track.service';
import { MatDialog } from '@angular/material';
import { User } from '../../dto/user';
import { HomeComponent } from '../home/home.component';
import { PlayService } from '../../services/play.service';
import { AudioPlayerComponent } from '../audio-player/audio-player.component';
import { MusicComponent } from '../music/music.component';

@Component({
  selector: 'audio-player-proxy',
  templateUrl: './audio-player-proxy.component.html',
  styleUrls: ['./audio-player-proxy.component.css']
})
export class AudioPlayerProxyComponent implements OnInit {

  @Input() track: Track;
  @Input() user: User;
  @Input() playlist: Track[];
  @Input() isRecommend: boolean;
  @Input() isHome: boolean;
  @Input() isGlobal: boolean;

  ngOnInit() {
  }

  constructor(public shared: SharedService,
              private trackService: TrackService,
              public dialog: MatDialog,
              private homeComponent: HomeComponent,
              private musicComponent: MusicComponent,
              private playService: PlayService,
              private audioPlayer: AudioPlayerComponent) {
    if (!this.user) {
      this.user = this.shared.getLoggedUser();
    }
  }

  public changeTrack(track: Track) {
    const dialogRef = this.dialog.open(ChangeTrackDialog, {
      width: '400px',
      data : track
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  }

  public rateTrack(track: Track) {
    this.trackService.rateTrack(track.id, this.user.id, track.tempRating)
        .subscribe(data => {
          const updatedTrack = new Track(data);
          track.rating = updatedTrack.rating;
          track.tempRating = null;
        });
  }

  public deleteTrackAction(track: Track) {
    this.homeComponent.deleteTrackFromUser(track);
  }

  public addTrackToUserFromRec(trackId: number) {
    this.homeComponent.addTrackToUserFromRec(trackId);
  }

  public addTrackToUser(track: Track) {
    this.musicComponent.addTrackToUser(track.id);
  }

  public isPlaying() {
    return this.audioPlayer.isPlaying() && this.audioPlayer.getCurrentTrack().id === this.track.id;
  }

  public playAudio() {
    if (this.audioPlayer.isPlaying()) {
      this.audioPlayer.stopAudio();
    }
    if (this.audioPlayer.getCurrentTrack() === null) {
      this.audioPlayer.initAudio(this.track);
    } else if (this.audioPlayer.getCurrentTrack().id !== this.track.id) {
      this.audioPlayer.initAudio(this.track);
    }
    this.audioPlayer.setCurrentPlaylist(this.playlist);
    this.audioPlayer.playAudio();
  }

  public stopAudio() {
    if (this.audioPlayer.isPlaying()) {
      this.audioPlayer.stopAudio();
    }
  }
}
