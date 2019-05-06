import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { Track } from '../../../dto/track';
import { TrackList } from '../../../dto/track-list';
import { TrackService } from '../../../services/track.service';
import { User } from '../../../dto/user';
import { SharedService } from '../../../services/shared.service';
import { PlaylistService } from '../../../services/playlist.service';
import { Playlist } from '../../../dto/playlist';

@Component({
    selector: 'change-playlist',
    templateUrl: 'change-playlist.html',
    styleUrls: ['../home.component.css']
})
export class ChangePlaylist {

    public title = '';
    public description = '';
    public playlist: Playlist;

    private user: User;
    private response: TrackList;
    public myMusic: Track[] = [];
    public tracksLength = 10;
    public pageEvent: any;
    public page = 0;
    public pageSize = 10;
    public pageSizeOptions: any = [10, 25, 50];

    constructor(
        public dialogRef: MatDialogRef<ChangePlaylist>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private trackService: TrackService,
        private shared: SharedService,
        private playlistService: PlaylistService) {
        this.title = data.title;
        this.description = data.description;
        this.playlist = data.playlist;
        this.user = shared.getLoggedUser();
        this.loadTracksList(null);
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

    addTrackToPlaylist(id: number) {
        this.playlistService.addTrackInPlaylist(this.playlist.id, id)
            .subscribe(data => {
               this.playlist = new Playlist(data);
            });
    }

    removeTrackFromPlaylist(id: number) {
        this.playlistService.removeTrackFromPlaylist(this.playlist.id, id)
            .subscribe(data => {
                this.playlist = new Playlist(data);
            });
    }
}
