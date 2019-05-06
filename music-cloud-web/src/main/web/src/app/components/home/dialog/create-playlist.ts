import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
    selector: 'create-playlist',
    templateUrl: 'create-playlist.html',
    styleUrls: ['../home.component.css']
})
export class CreatePlaylist {

    public title = '';
    public description = '';
    public name = '';

    constructor(
        public dialogRef: MatDialogRef<CreatePlaylist>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
        this.title = data.title;
        this.description = data.description;
    }

    public onNoClick(): void {
        this.dialogRef.close();
    }
}
