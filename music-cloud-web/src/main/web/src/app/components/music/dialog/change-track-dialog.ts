import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { Track } from '../../../dto/track';
import { TrackService } from '../../../services/track.service';
import { Router } from '@angular/router';

@Component({
    selector: 'change-track-dialog',
    templateUrl: 'change-track-dialog.html',
    styleUrls: ['../music.component.css']
})
export class ChangeTrackDialog {

    backupTrack: Track;
    isValid = true;

    constructor(
        private trackService: TrackService,
        private router: Router,
        public dialogRef: MatDialogRef<ChangeTrackDialog>,
        @Inject(MAT_DIALOG_DATA) public track: Track) {
        this.backupTrack = new Track(track.toObject());
    }

    onYesClick(): void {
        if (this.isValidInput()) {
            this.isValid = true;
            this.trackService.updateTrack(this.track.id, this.track.toObject())
                .subscribe(data => {
                    const updatedTrack = new Track(data);
                    this.track.title = updatedTrack.title;
                    this.track.artist = updatedTrack.artist;
                    this.track.album = updatedTrack.album;
                    this.track.year = updatedTrack.year;
                    this.dialogRef.close();
                });
        } else {
            this.isValid = false;
        }
    }

    onNoClick(): void {
        this.dialogRef.close();
        this.track.title = this.backupTrack.title;
        this.track.artist = this.backupTrack.artist;
        this.track.album = this.backupTrack.album;
        this.track.year = this.backupTrack.year;
    }

    isValidInput() {
        return (this.track.title !== '')  && (this.track.artist !== '') && (this.track.album !== '') && (this.track.year !== '');
    }

    gotoAdditional() {
        this.dialogRef.close();
        this.router.navigate(['additional/' + this.track.id]);
    }
}
