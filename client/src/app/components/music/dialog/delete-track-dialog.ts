import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { Track } from '../../../dto/track';
import { TrackService } from '../../../services/track.service';

@Component({
    selector: 'delete-track-dialog',
    templateUrl: 'delete-track-dialog.html',
})
export class DeleteTrackDialog {

    constructor(
        private tracksService : TrackService,
        public dialogRef: MatDialogRef<DeleteTrackDialog>,
        @Inject(MAT_DIALOG_DATA) public track: Track) {}

    onYesClick(): void {
        this.tracksService.deleteTrack(this.track.id).subscribe(() => {
            this.dialogRef.close();
        });
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}
