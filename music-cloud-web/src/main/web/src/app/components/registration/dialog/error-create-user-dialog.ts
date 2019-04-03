import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
    selector: 'error-create-user-dialog',
    templateUrl: 'error-create-user-dialog.html',
})
export class ErrorCreateUserDialog {

    constructor(
        public dialogRef: MatDialogRef<ErrorCreateUserDialog>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}
