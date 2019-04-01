import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
    selector: 'create-track-dialog',
    templateUrl: 'create-track-dialog.html',
})
export class CreateTrackDialog {

    constructor(
        public dialogRef: MatDialogRef<CreateTrackDialog>,
        @Inject(MAT_DIALOG_DATA) public data: any) {}
}
