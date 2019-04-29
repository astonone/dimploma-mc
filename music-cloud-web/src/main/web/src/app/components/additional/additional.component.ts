import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TrackService } from '../../services/track.service';
import { GenreService } from '../../services/genre.service';
import { MoodService } from '../../services/mood.service';
import { GenreList } from '../../dto/genre-list';
import { MoodList } from '../../dto/mood-list';
import { Genre } from '../../dto/genre';
import { Mood } from '../../dto/mood';
import { TrackFullInfo } from '../../dto/track-full-info';
import { SharedService } from '../../services/shared.service';
import { MatDialog } from '@angular/material';
import { InfoDialog } from '../home/dialog/info-dialog';

@Component({
  selector: 'app-additional',
  templateUrl: './additional.component.html',
  styleUrls: ['./additional.component.css']
})
export class AdditionalComponent implements OnInit {

  public track: TrackFullInfo = TrackFullInfo.createEmptyObject();
  public genres: Genre[] = [];
  public moods: Mood[] = [];
  public selectedGenre:  Genre;
  public selectedMood: Mood;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private trackService: TrackService,
              private genreService: GenreService,
              private moodService: MoodService,
              private shared: SharedService,
              public dialog: MatDialog) {
    const trackId = this.route.snapshot.paramMap.get('id');
    this.loadTrack(trackId);
    this.loadGenres();
    this.loadMoods();
  }

  ngOnInit() {
      if (this.shared.getLoggedUser() === null) {
          this.router.navigate(['login']);
      }
  }

  private loadTrack(trackId: string) {
      this.trackService.getTrackFullInfo(Number(trackId)).subscribe(data => {
          this.track = new TrackFullInfo(data);
      });
  }

  private loadGenres() {
      this.genreService.getAllGenres().subscribe(data => {
          const allGenres: GenreList = new GenreList(data);
          this.genres = allGenres.genres;
      });
  }

  private loadMoods() {
      this.moodService.getAllMoods().subscribe(data => {
          const allMoods: MoodList = new MoodList(data);
          this.moods = allMoods.moods;
      });
  }

  public addTrackGenre() {
    if (this.selectedGenre) {
       this.trackService.addTrackGenre(this.track.id, this.selectedGenre.id)
           .subscribe(data => {
               this.openInfoDialog({title: 'Успешно', description: 'Жанр добавлен к треку'});
                this.track = new TrackFullInfo(data);
       }, error1 => {
            this.openInfoDialog({title: 'Ошибка', description: 'Произошла ошибка:' + error1.error.message});
       });
    }
  }

  public removeTrackGenre() {
      if (this.selectedGenre) {
          this.trackService.removeTrackGenre(this.track.id, this.selectedGenre.id)
              .subscribe(data => {
                  this.openInfoDialog({title: 'Успешно', description: 'Жанр отвязан от трека'});
                  this.track = new TrackFullInfo(data);
              }, error1 => {
                  this.openInfoDialog({title: 'Ошибка', description: 'Произошла ошибка:' + error1.error.message});
              });
      }
  }

  public addTrackMood() {
      if (this.selectedMood) {
          this.trackService.addTrackMood(this.track.id, this.selectedMood.id)
              .subscribe(data => {
                  this.openInfoDialog({title: 'Успешно', description: 'Настроение добавлено к треку'});
                  this.track = new TrackFullInfo(data);
              }, error1 => {
                  this.openInfoDialog({title: 'Ошибка', description: 'Произошла ошибка:' + error1.error.message});
              });
      }
  }

  public removeTrackMood() {
      if (this.selectedMood) {
          this.trackService.removeTrackMood(this.track.id, this.selectedMood.id)
              .subscribe(data => {
                  this.openInfoDialog({title: 'Успешно', description: 'Настроение отвязано от трека'});
                  this.track = new TrackFullInfo(data);
              }, error1 => {
                  this.openInfoDialog({title: 'Ошибка', description: 'Произошла ошибка:' + error1.error.message});
              });
      }
  }

  openInfoDialog(data: any): void {
      const dialogRef = this.dialog.open(InfoDialog, {
          data : data
      });
      dialogRef.afterClosed().subscribe(result => {
      });
  }
}
