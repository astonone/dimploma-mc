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
import { AddTrackToUserDialog } from '../music/dialog/add-track-to-user-dialog';
import { UserList } from '../../dto/user-list';
import { FriendDialog } from './dialog/friend-dialog';

@Component({
    selector: 'home',
    templateUrl: './home.component.html',
    styleUrls: [
        './home.component.css'
    ]
})

export class HomeComponent implements OnInit {

    public user: User;

    public myFriends: User[] = [];
    public myRequest: User[] = [];

    public myMusic: Track[] = [];
    public recommendedMusic: Track[] = [];
    public myPlaylists: any[] = [];
    private response: TrackList;
    private responseFriendRequests: UserList;
    private responseFriends: UserList;
    public tracksLength = 10;
    public pageEvent: any;
    public page = 0;
    public pageSize = 10;
    public pageSizeOptions: any = [10, 25, 50];
    public photos: Observable<string[]>;

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
                        this.loadFriendRequests();
                        this.loadFriends();
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
            this.loadFriendRequests();
            this.loadFriends();
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

    openTrackCreatedDialog(response: any): void {
        const dialogRef = this.dialog.open(AddTrackToUserDialog, {
            width: '250px',
            data : response
        });
        dialogRef.afterClosed().subscribe(result => {
        });
    }

    isEmptyPhotoLink() {
        return this.user.email ? this.user.isEmptyPhotoLink() : false;
    }

    printUserName() {
        return this.user.email ? this.user.printUserName() : '';
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

    deleteTrackFromUser(track: Track) {
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

    changeTrack(track: Track) {
        const dialogRef = this.dialog.open(ChangeTrackDialog, {
            width: '400px',
            data : track
        });
        dialogRef.afterClosed().subscribe(result => {
        });
    }

    rateTrack(track: Track) {
        this.trackService.rateTrack(track.id, this.user.id, track.tempRating)
            .subscribe(data => {
                const updatedTrack = new Track(data);
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
        const index = tracks.map(x => {
            return x.id;
        }).indexOf(trackId);

        tracks.splice(index, 1);
    }

    loadFriendRequests() {
        this.myRequest = [];
        this.userService.getAllFriendRequests(this.user.id)
            .subscribe(data => {
                this.responseFriendRequests = new UserList(data);
                this.responseFriendRequests.users.forEach(user => {
                    this.myRequest.push(new User(user));
                });
            });
    }

    loadFriends() {
        this.myFriends = [];
        this.userService.getAllFriends(this.user.id)
            .subscribe(data => {
                this.responseFriends = new UserList(data);
                this.responseFriends.users.forEach(user => {
                    this.myFriends.push(new User(user));
                });
            });
    }

    gotoProfile(id: number) {
        this.router.navigate(['user/' + id]);
    }

    showUserInfo(user: User) {
        const firstName = user.userDetails.firstName === null ? '' : user.userDetails.firstName;
        const lastName = user.userDetails.lastName === null ? '' : user.userDetails.lastName;
        return firstName === '' && lastName === '' ? user.email : firstName + ' ' + lastName;
    }

    addFriend(user: User) {
        this.userService.addFriend(this.user.id, user.id)
            .subscribe(() => {
                this.openFriendDialog({title: 'Друзья', description: 'Пользователь добавлен в друзья'});
                this.loadFriendRequests();
                this.loadFriends();
            }, error => {
                this.openFriendDialog({title: 'Ошибка', description: 'Произошла ошибка:' + error.error.message});
                console.log(error);
            });
    }

    cancelFriendRequest(user: User) {
        this.userService.cancelFriendRequest(this.user.id, user.id)
            .subscribe(() => {
                this.openFriendDialog({title: 'Друзья', description: 'Заявка была отлонена'});
                this.loadFriendRequests();
            }, error => {
                this.openFriendDialog({title: 'Ошибка', description: 'Произошла ошибка:' + error.error.message});
            });
    }

    openFriendDialog(data: any): void {
        const dialogRef = this.dialog.open(FriendDialog, {
            data : data
        });
        dialogRef.afterClosed().subscribe(result => {
        });
    }
}
