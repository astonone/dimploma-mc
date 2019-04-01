import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
    selector: 'delete-track-dialog',
    templateUrl: 'delete-track-dialog.html',
})
export class DeleteTrackDialog {

    constructor(
        public dialogRef: MatDialogRef<DeleteTrackDialog>,
        @Inject(MAT_DIALOG_DATA) public data: any) {}

    onYesClick(): void {
        this.dialogRef.close();
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}
