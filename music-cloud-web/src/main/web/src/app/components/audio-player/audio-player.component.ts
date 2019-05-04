import { Component, OnInit } from '@angular/core';
import { Track } from '../../dto/track';
import { PlayService } from '../../services/play.service';

@Component({
  selector: 'audio-player',
  templateUrl: './audio-player.component.html',
  styleUrls: ['./audio-player.component.css']
})
export class AudioPlayerComponent implements OnInit {

  public track: Track = null;
  public playlist: Track[] = [];
  public trackTitle: string;

  constructor(private playService: PlayService) {
  }

  ngOnInit() {
  }

  public getTrackTitle() {
    this.track = this.playService.getCurrentTrack();
    if (this.track == null) {
      this.trackTitle = 'Ничего не проигрывается';
    } else {
      this.trackTitle = this.track.title + ' - ' + this.track.artist;
    }
    return this.trackTitle;
  }

  public getCurrentTrack() {
    return this.playService.getCurrentTrack();
  }

  public getCurrentPlaylist() {
    return this.playlist;
  }

  public setCurrentPlaylist(tracks: Track[]) {
    this.playlist = tracks;
    this.playService.setCurrentPlaylist(this.playlist);
  }

  public isPlaying() {
    return this.playService.isPlaying();
  }

  public initAudio(track: Track) {
    this.playService.setCurrentPlaylist(this.playlist);
    this.track = track;
    this.trackTitle = this.track.title + ' - ' + this.track.artist;
    this.playService.initAudio(track);
  }

  public playAudio() {
    this.playService.playAudio();
  }

  public stopAudio() {
    this.playService.stopAudio();
  }

  public nextAudio() {
    this.playService.nextAudio();
  }

  public previousAudio() {
    this.playService.previousAudio();
  }
}
