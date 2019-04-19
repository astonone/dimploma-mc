import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { TrackService } from '../../../services/track.service';

@Component({
    selector: 'delete-track-dialog',
    templateUrl: 'delete-track-dialog.html',
})
export class DeleteTrackDialog {

    constructor(
        private trackService: TrackService,
        public dialogRef: MatDialogRef<DeleteTrackDialog>,
        @Inject(MAT_DIALOG_DATA) public data: any) {}

    onYesClick(): void {
        if (this.data.isUser) {
            this.trackService.deleteTrackFromUser(this.data.user.id, this.data.track.id).subscribe(() => {
                if (this.data.tracks.length === 1) {
                    this.data.deleteTrackFromList(this.data.track.id, this.data.tracks);
                } else {
                    this.data.musicComponent.loadTracksList(null);
                }
            });
        } else {
            this.trackService.deleteTrack(this.data.track.id).subscribe(() => {
                if (this.data.tracks.length === 1) {
                    this.data.deleteTrackFromList(this.data.track.id, this.data.tracks);
                } else {
                    this.data.musicComponent.loadTracksList(null);
                }
            });
        }
        this.dialogRef.close();
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}
