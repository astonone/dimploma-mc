import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { Router } from '@angular/router';

@Component({
    selector: 'create-user-dialog',
    templateUrl: 'create-user-dialog.html',
})
export class CreateUserDialog {

    constructor(
        private router : Router,
        public dialogRef: MatDialogRef<CreateUserDialog>,
        @Inject(MAT_DIALOG_DATA) public data: any) {}

    onNoClick(): void {
        this.dialogRef.close();
    }

    onYesClick(): void {
        this.dialogRef.close();
        this.router.navigate(['login']);
    }
}
