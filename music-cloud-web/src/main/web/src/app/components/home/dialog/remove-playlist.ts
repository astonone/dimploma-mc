import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
    selector: 'remove-playlist',
    templateUrl: 'remove-playlist.html',
    styleUrls: ['../home.component.css']
})
export class RemovePlaylist {

    public title = '';
    public description = '';
    public id: number = null;

    constructor(
        public dialogRef: MatDialogRef<RemovePlaylist>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
        this.title = data.title;
        this.description = data.description;
        this.id = data.playlistId;
    }

    public onNoClick(): void {
        this.dialogRef.close();
    }
}
