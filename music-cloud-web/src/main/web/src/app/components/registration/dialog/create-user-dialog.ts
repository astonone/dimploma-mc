import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { Router } from '@angular/router';

@Component({
    selector: 'create-user-dialog',
    templateUrl: 'create-user-dialog.html',
    styleUrls: ['../registration.component.css']
})
export class CreateUserDialog {

    constructor(
        private router: Router,
        public dialogRef: MatDialogRef<CreateUserDialog>,
        @Inject(MAT_DIALOG_DATA) public data: any) {}

    public onNoClick(): void {
        this.dialogRef.close();
    }

    public onYesClick(): void {
        this.dialogRef.close();
        this.router.navigate(['login']);
    }
}
