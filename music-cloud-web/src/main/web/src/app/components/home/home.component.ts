import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { SharedService } from '../../services/shared.service';
import { User } from '../../dto/user';
import { Track } from '../../dto/track';
import { TrackList } from '../../dto/track-list';
import { TrackService } from '../../services/track.service';
import { DeleteTrackDialog } from '../music/dialog/delete-track-dialog';
import { MatDialog } from '@angular/material';
import { FileService } from '../../services/file.service';
import { Observable } from 'rxjs';
import { AddTrackToUserDialog } from '../music/dialog/add-track-to-user-dialog';
import { UserList } from '../../dto/user-list';
import { InfoDialog } from './dialog/info-dialog';
import { PlaylistService } from '../../services/playlist.service';
import { PlaylistList } from '../../dto/playlist-list';
import { Playlist } from '../../dto/playlist';
import { RemovePlaylist } from './dialog/remove-playlist';
import { CreatePlaylist } from './dialog/create-playlist';
import { ChangePlaylist } from './dialog/change-playlist';

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
    public myPlaylists: Playlist[] = [];
    private response: TrackList;
    private responseFriendRequests: UserList;
    private responseFriends: UserList;
    public tracksLength = 10;
    public pageEvent: any;
    public page = 0;
    public pageSize = 10;
    public pageSizeOptions: any = [10, 25, 50];
    public photos: Observable<string[]>;
    public audio: any;

    constructor(private router: Router,
                private userService: UserService,
                private shared: SharedService,
                private trackService: TrackService,
                public dialog: MatDialog,
                private fileService: FileService,
                private playlistService: PlaylistService) {
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
                        this.loadPlaylists();
                    },
                    error => {
                        if (error.status === 401) {
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
            this.loadPlaylists();
        }
        } else {
            this.router.navigate(['music']);
        }
    }

    public addTrackToUser(id: number) {
        this.trackService.addTrackToUser(this.user.id, id)
            .subscribe(data => {
                this.openTrackCreatedDialog(null);
            });
    }

    public addTrackToUserFromRec(id: number) {
        this.trackService.addTrackToUser(this.user.id, id)
            .subscribe(data => {
                this.openTrackCreatedDialog(null);
                this.loadTracksList(null);
            });
    }

    private openTrackCreatedDialog(response: any): void {
        const dialogRef = this.dialog.open(AddTrackToUserDialog, {
            width: '250px',
            data : response
        });
        dialogRef.afterClosed().subscribe(result => {
        });
    }

    public isEmptyPhotoLink() {
        return this.user.email ? this.user.isEmptyPhotoLink() : false;
    }

    public printUserName() {
        return this.user.email ? this.user.printUserName() : '';
    }

    private loadTracksList(event) {
        if (event) {
            this.trackService.getUserTracks(this.user.id, event.pageIndex, event.pageSize)
                .subscribe(data => {
                    this.response = new TrackList(data);
                    this.tracksLength = this.response.allCount;
                    this.myMusic = this.response.tracks;
                });
        } else {
            this.trackService.getUserTracks(this.user.id, this.page, this.pageSize)
                .subscribe(data => {
                this.response = new TrackList(data);
                this.tracksLength = this.response.allCount;
                this.myMusic = this.response.tracks;
            });
        }
    }

    private loadRecommendedTracksList() {
        this.trackService.getRecommendedUserTracks(this.user.id)
            .subscribe(data => {
                this.response = new TrackList(data);
                this.recommendedMusic = this.response.tracks;
                this.loadAudioFiles(this.recommendedMusic);
            });
    }

    private loadAudioFiles(tracks: Track[]) {
        for (let i = 0; i < tracks.length; i++) {
            this.loadFile(tracks[i]);
        }
    }

    public deleteTrackFromUser(track: Track) {
        const dialogRef = this.dialog.open(DeleteTrackDialog, {
            width: '250px',
            data : null
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.trackService.deleteTrackFromUser(this.user.id, track.id)
                    .subscribe(() => {
                        this.loadTracksList(null);
                    });
            }
        });
    }

    private getPhoto() {
        this.photos = this.fileService.getUploadedPhoto(this.user.getPhotoLink());
    }

    private loadFile(track: Track) {
        track.files = this.fileService.getUploadedTrack(track.filename);
    }

    private loadFriendRequests() {
        this.myRequest = [];
        this.userService.getAllFriendRequests(this.user.id)
            .subscribe(data => {
                this.responseFriendRequests = new UserList(data);
                this.responseFriendRequests.users.forEach(user => {
                    this.myRequest.push(new User(user));
                });
            });
    }

    private loadFriends() {
        this.myFriends = [];
        this.userService.getAllFriends(this.user.id)
            .subscribe(data => {
                this.responseFriends = new UserList(data);
                this.responseFriends.users.forEach(user => {
                    this.myFriends.push(new User(user));
                });
            });
    }

    public gotoProfile(id: number) {
        this.router.navigate(['user/' + id]);
    }

    public showUserInfo(user: User) {
        const firstName = user.userDetails.firstName === null ? '' : user.userDetails.firstName;
        const lastName = user.userDetails.lastName === null ? '' : user.userDetails.lastName;
        return firstName === '' && lastName === '' ? user.email : firstName + ' ' + lastName;
    }

    public addFriend(user: User) {
        this.userService.addFriend(this.user.id, user.id)
            .subscribe(() => {
                this.openFriendDialog({title: 'Друзья', description: 'Пользователь добавлен в друзья'});
                this.loadFriendRequests();
                this.loadFriends();
            }, error => {
                this.openFriendDialog({title: 'Ошибка', description: 'Произошла ошибка:' + error.error.message});
            });
    }

    public cancelFriendRequest(user: User) {
        this.userService.cancelFriendRequest(this.user.id, user.id)
            .subscribe(() => {
                this.openFriendDialog({title: 'Друзья', description: 'Заявка была отлонена'});
                this.loadFriendRequests();
            }, error => {
                this.openFriendDialog({title: 'Ошибка', description: 'Произошла ошибка:' + error.error.message});
            });
    }

    private openFriendDialog(data: any): void {
        const dialogRef = this.dialog.open(InfoDialog, {
            data : data
        });
        dialogRef.afterClosed().subscribe(result => {
        });
    }

    private loadPlaylists() {
        this.playlistService.getAllPlaylists(this.user.id)
            .subscribe(data => {
               this.myPlaylists = new PlaylistList(data).playlists;
            });
    }

    changePlaylist(playlist: Playlist) {
        this.openChangePlaylistDialog({title: 'Настройка', description: 'Здесь вы сможете добавить или удалить треки из плейлиста',
            playlist: playlist});
    }

    private openChangePlaylistDialog(data: any): void {
        const dialogRef = this.dialog.open(ChangePlaylist, {
            data : data
        });
        dialogRef.afterClosed().subscribe(result => {
            this.loadPlaylists();
        });
    }

    removePlaylist(playlist: Playlist) {
        this.openRemovePlaylistDialog({title: 'Удаление', description: 'Вы действительно хотите удалить плейлист?',
            playlistId: playlist.id});
    }

    private openRemovePlaylistDialog(data: any): void {
        const dialogRef = this.dialog.open(RemovePlaylist, {
            data : data
        });
        dialogRef.afterClosed().subscribe(id => {
            if (id) {
                this.playlistService.deletePlaylist(id)
                    .subscribe(() => {
                        this.loadPlaylists();
                    });
            }
        });
    }

    createPlaylist() {
        this.openCreatePlaylistDialog({title: 'Создание', description: 'Введите название для плейлиста'});
    }

    private openCreatePlaylistDialog(data: any): void {
        const dialogRef = this.dialog.open(CreatePlaylist, {
            data : data
        });
        dialogRef.afterClosed().subscribe(name => {
            if (name) {
                this.playlistService.createPlaylist(name, this.user.id)
                    .subscribe(() => {
                        this.loadPlaylists();
                    });
            }
        });
    }

    openDialogs() {
        this.router.navigate(['dialogs/' + this.user.id]);
    }
}
