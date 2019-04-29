import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
    selector: 'error-create-user-dialog',
    templateUrl: 'error-create-user-dialog.html',
    styleUrls: ['../registration.component.css']
})
export class ErrorCreateUserDialog {

    constructor(
        public dialogRef: MatDialogRef<ErrorCreateUserDialog>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    public onNoClick(): void {
        this.dialogRef.close();
    }
}
