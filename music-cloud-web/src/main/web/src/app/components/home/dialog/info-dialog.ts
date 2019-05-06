import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
    selector: 'info-dialog',
    templateUrl: 'info-dialog.html',
    styleUrls: ['../home.component.css']
})
export class InfoDialog {

    public title = '';
    public description = '';

    constructor(
        public dialogRef: MatDialogRef<InfoDialog>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
        this.title = data.title;
        this.description = data.description;
    }

    public onYesClick(): void {
        this.dialogRef.close();
    }
}
