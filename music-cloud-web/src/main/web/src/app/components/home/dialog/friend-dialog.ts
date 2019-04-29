import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
    selector: 'friend-dialog',
    templateUrl: 'friend-dialog.html',
    styleUrls: ['../home.component.css']
})
export class FriendDialog {

    public title = '';
    public description = '';

    constructor(
        public dialogRef: MatDialogRef<FriendDialog>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
        this.title = data.title;
        this.description = data.description;
    }

    public onYesClick(): void {
        this.dialogRef.close();
    }
}
