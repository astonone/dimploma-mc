import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { SharedService } from '../../services/shared.service';
import { User } from '../../dto/user';
import { Track } from '../../dto/track';
import { TrackList } from '../../dto/track-list';
import { TrackService } from '../../services/track.service';
import { DeleteTrackDialog } from '../music/dialog/delete-track-dialog';
import { ChangeTrackDialog } from '../music/dialog/change-track-dialog';
import { MatDialog } from '@angular/material';
import { FileService } from '../../services/file.service';
import { Observable } from 'rxjs';
import {AddTrackToUserDialog} from '../music/dialog/add-track-to-user-dialog';

@Component({
    selector: 'home',
    templateUrl: './home.component.html',
    styleUrls: [
        './home.component.css'
    ]
})

export class HomeComponent implements OnInit {
    user: User;

    myFriends: any = [];
    myRequest: any = [];

    myMusic: Track[] = [];
    recommendedMusic: Track[] = [];
    myPlaylists: any[] = [];
    response: TrackList;
    tracksLength : number = 10;
    pageEvent : any;
    page: number = 0;
    pageSize : number = 10;
    pageSizeOptions : any = [10,25,50,10];
    photos : Observable<string[]>;

    constructor(private router: Router,
                private userService: UserService,
                private shared: SharedService,
                private trackService: TrackService,
                public dialog: MatDialog,
                private fileService: FileService) {
        this.user = this.shared.createEmptyUserStub();
    }

    ngOnInit() {
        if (this.shared.getStorage().getItem('token') !== null && this.shared.getStorage().getItem('token')) {
        if (this.shared.getStorage().getItem('loggedUser') === '') {
            this.userService.auth()
                .subscribe(data => {
                        this.shared.getStorage().setItem('loggedUser', JSON.stringify(data));
                        this.shared.setLoggedUser();
                        this.user = new User(data);
                        this.getPhoto();
                        this.loadTracksList(null);
                        this.loadRecommendedTracksList();
                    },
                    error => {
                        if (error.status == 401) {
                            this.shared.logout();
                        }
                    }
                );
        } else {
            this.user = new User(JSON.parse(this.shared.getStorage().getItem('loggedUser')));
            this.getPhoto();
            this.loadTracksList(null);
            this.loadRecommendedTracksList();
        }
        } else {
            this.router.navigate(['login']);
        }
    }

    addTrackToUser(id: number) {
        this.trackService.addTrackToUser(this.user.id, id)
            .subscribe(data => {
                this.openTrackCreatedDialog(null);
            });
    }

    addTrackToUserFromRec(id: number) {
        this.trackService.addTrackToUser(this.user.id, id)
            .subscribe(data => {
                this.openTrackCreatedDialog(null);
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

    isEmptyPhotoLink() {
        return this.user.email !== '' ? this.user.isEmptyPhotoLink() : false;
    }

    printUserName() {
        return this.user.email !== '' ? this.user.printUserName() : '';
    }

    loadTracksList(event) {
        if (event) {
            this.trackService.getUserTracks(this.user.id, event.pageIndex, event.pageSize)
                .subscribe(data => {
                    this.response = new TrackList(data);
                    this.tracksLength = this.response.allCount;
                    this.myMusic = this.response.tracks;
                    this.loadAudioFiles(this.myMusic);
                });
        } else {
            this.trackService.getUserTracks(this.user.id, this.page, this.pageSize)
                .subscribe(data => {
                this.response = new TrackList(data);
                this.tracksLength = this.response.allCount;
                this.myMusic = this.response.tracks;
                this.loadAudioFiles(this.myMusic);
            });
        }
    }

    loadRecommendedTracksList() {
        this.trackService.getRecommendedUserTracks(this.user.id)
            .subscribe(data => {
                this.response = new TrackList(data);
                this.recommendedMusic = this.response.tracks;
                this.loadAudioFiles(this.recommendedMusic);
            });
    }

    loadAudioFiles(tracks: Track[]) {
        for (let i = 0; i < tracks.length; i++) {
            this.loadFile(tracks[i]);
        }
    }

    deleteTrackFromUser(track : Track) {
        const dialogRef = this.dialog.open(DeleteTrackDialog, {
            width: '250px',
            data : null
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.trackService.deleteTrackFromUser(this.user.id, track.id)
                    .subscribe(() => {
                        if (this.myMusic.length === 1) {
                            this.deleteTrack(track.id, this.myMusic);
                        } else {
                            this.loadTracksList(null);
                        }
                    });
            }
        });
    }

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

    getPhoto() {
        this.photos = this.fileService.getUploadedPhoto(this.user.getPhotoLink());
    }

    loadFile(track: Track) {
        track.files = this.fileService.getUploadedTrack(track.filename);
    }

    deleteTrack(trackId: number, tracks: Track[]) {
        let index = tracks.map(x => {
            return x.id;
        }).indexOf(trackId);

        tracks.splice(index, 1);
    }
}
