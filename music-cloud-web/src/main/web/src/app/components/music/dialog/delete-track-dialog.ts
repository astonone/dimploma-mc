import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
    selector: 'delete-track-dialog',
    templateUrl: 'delete-track-dialog.html',
})
export class DeleteTrackDialog {

    isOk : boolean = true;

    constructor(
        public dialogRef: MatDialogRef<DeleteTrackDialog>,
        @Inject(MAT_DIALOG_DATA) public data: any) {}

    onNoClick(): void {
        this.dialogRef.close();
    }
}
