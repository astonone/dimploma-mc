import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { Track } from '../../../dto/track';
import { TrackService } from '../../../services/track.service';

@Component({
    selector: 'change-track-dialog',
    templateUrl: 'change-track-dialog.html',
})
export class ChangeTrackDialog {

    constructor(
        private tracksService : TrackService,
        public dialogRef: MatDialogRef<ChangeTrackDialog>,
        @Inject(MAT_DIALOG_DATA) public track: Track) {}

    onYesClick(): void {
        this.dialogRef.close();
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}
