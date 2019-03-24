import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
    selector: 'create-track-dialog',
    templateUrl: 'create-track-dialog.html',
})
export class CreateTrackDialog {

    constructor(
        private router : Router,
        public dialogRef: MatDialogRef<CreateTrackDialog>,
        @Inject(MAT_DIALOG_DATA) public data: any) {}

    onNoClick(): void {
        this.dialogRef.close();
    }

    onYesClick(): void {
        this.dialogRef.close();
        this.router.navigate(['music']);
    }
}
