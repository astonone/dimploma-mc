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
import { Router } from '@angular/router';
import { Genre } from '../../dto/genre';
import { Mood } from '../../dto/mood';
import { GenreService } from '../../services/genre.service';
import { MoodService } from '../../services/mood.service';
import { GenreList } from '../../dto/genre-list';
import { MoodList } from '../../dto/mood-list';

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
  pageSizeOptions : any = [10,25,50];
  //filters
  title: string = '';
  artist: string = '';
  genres: Genre[] = [];
  moods: Mood[] = [];
  selectedGenres:  Genre[] = [];
  selectedMoods: Mood[] = [];

  ngOnInit() {
    if (this.shared.getLoggedUser() === null) {
      this.router.navigate(['login']);
    }
    this.loadTracksList(null);
    this.loadGenres();
    this.loadMoods();
  }

  constructor(private trackService: TrackService,
              public dialog: MatDialog,
              public shared: SharedService,
              private fileService: FileService,
              private router: Router,
              private genreService: GenreService,
              private moodService: MoodService) {
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
      data : null
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.trackService.deleteTrack(track.id).subscribe(() => {
          if (this.tracks.length === 1) {
            this.deleteTrackFromList(track.id, this.tracks);
          } else {
            this.loadTracksList(null);
          }
        });
      }
    });
  }

  openTrackCreatedDialog(response : any) : void {
    const dialogRef = this.dialog.open(AddTrackToUserDialog, {
      data : response
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  };

  changeTrack(track : Track) {
    const dialogRef = this.dialog.open(ChangeTrackDialog, {
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

  deleteTrackFromList(trackId: number, tracks: Track[]) {
    let index = tracks.map(x => {
      return x.id;
    }).indexOf(trackId);

    tracks.splice(index, 1);
  }

  findTracks() {
    let genresObj = [];
    let moodsObj = [];
    this.selectedGenres.forEach(genre => genresObj.push(genre.toObject()));
    this.selectedMoods.forEach(mood => moodsObj.push(mood.toObject()));
    let request = {
      title: this.title,
      artist: this.artist,
      genres: genresObj,
      moods: moodsObj
    };
    this.trackService.findTracks(request, this.page, this.pageSize).subscribe(data => {
      this.response = new TrackList(data);
      this.tracksLength = this.response.allCount;
      this.tracks = this.response.tracks;
      this.loadAudioFiles();
    });
  }

  clearFilters() {
    this.title = '';
    this.artist = '';
    this.selectedGenres = [];
    this.selectedMoods = [];
    this.loadTracksList(null);
  }

  loadGenres() {
    this.genreService.getAllGenres().subscribe(data => {
      let allGenres : GenreList = new GenreList(data);
      this.genres = allGenres.genres;
    });
  }

  loadMoods() {
    this.moodService.getAllMoods().subscribe(data => {
      let allMoods : MoodList = new MoodList(data);
      this.moods = allMoods.moods;
    });
  }
}
