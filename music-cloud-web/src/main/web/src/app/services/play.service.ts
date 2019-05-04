import { Injectable } from '@angular/core';
import { Track } from '../dto/track';
import { FileService } from './file.service';

@Injectable({
    providedIn: 'root'
})
export class PlayService {
    private isPlay = false;
    private currentIndex = 0;
    private currentAudio: any;
    private currentTrack: Track = null;
    private playlist: Track[];

    constructor(private fileService: FileService) {
    }

    private loadFile(track: Track) {
        track.files = this.fileService.getUploadedTrack(track.filename);
    }

    public initAudio(track: Track) {
        this.currentTrack = track;
        this.loadFile(this.currentTrack);
        this.currentAudio = new Audio();
        track.files.subscribe(file => {
            this.currentAudio.src = file[0];
        });
    }

    public getCurrentTrack() {
        return this.currentTrack;
    }

    public isPlaying() {
        return this.isPlay;
    }

    public getCurrentPlaylist() {
        return this.playlist;
    }

    public setCurrentPlaylist(tracks: Track[]) {
        this.playlist = tracks;
    }

    public playAudio() {
        if (this.currentTrack !== null) {
            this.isPlay = true;
            this.currentAudio.load();
            this.currentAudio.play();

            this.currentIndex = this.playlist.map(x => {
                return x.id;
            }).indexOf(this.currentTrack.id);
        }
    }

    public stopAudio() {
        this.isPlay = false;
        this.currentAudio.pause();
    }

    public nextAudio() {
        if (this.isPlay) {
            this.currentAudio.pause();
            if (this.currentIndex < this.playlist.length - 1) {
                this.currentIndex++;
            } else {
                this.currentIndex = 0;
            }
            this.currentTrack = this.playlist[this.currentIndex];
            this.loadFile(this.currentTrack);
            this.currentAudio = new Audio();
            this.currentTrack.files.subscribe(file => {
                this.currentAudio.src = file[0];
            });
            this.currentAudio.load();
            this.currentAudio.play();
        }
    }

    public previousAudio() {
        if (this.isPlay) {
            this.currentAudio.pause();
            if (this.currentIndex > 0) {
                this.currentIndex--;
            } else {
                this.currentIndex = this.playlist.length - 1;
            }
            this.currentTrack = this.playlist[this.currentIndex];
            this.loadFile(this.currentTrack);
            this.currentAudio = new Audio();
            this.currentTrack.files.subscribe(file => {
                this.currentAudio.src = file[0];
            });
            this.currentAudio.load();
            this.currentAudio.play();
        }
    }
}
