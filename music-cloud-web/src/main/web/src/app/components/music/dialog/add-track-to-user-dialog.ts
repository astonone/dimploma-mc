import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
    selector: 'add-track-to-user-dialog',
    templateUrl: 'add-track-to-user-dialog.html',
})
export class AddTrackToUserDialog {

    constructor(
        public dialogRef: MatDialogRef<AddTrackToUserDialog>,
        @Inject(MAT_DIALOG_DATA) public data: any) {}

    onYesClick(): void {
        this.dialogRef.close();
    }
}
