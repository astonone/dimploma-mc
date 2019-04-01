import { Component, OnInit } from '@angular/core';
import { TrackService } from '../../services/track.service';
import { CreateTrackDialog } from './dialog/create-track-dialog';
import { MatDialog } from '@angular/material';

@Component({
  selector: 'app-download',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {

  uploadedTrack : any = {};
  isNotChoosed : boolean;
  isError : boolean;

  constructor(private trackService: TrackService,
              public dialog: MatDialog) { }

  ngOnInit() {
    this.isNotChoosed = true;
    this.isError = false;
  }

  setFileForUpload = function(files) {
    let fd = new FormData();
    fd.append("uploadedFile", files[0]);
    this.uploadedTrack = fd;
    this.isNotChoosed = false;
    this.isError = false;
  };

  uploadFile = function() {
    if (!this.isNotChoosed) {
    this.trackService.upload(this.uploadedTrack)
        .subscribe(data => {
          this.isError = false;
          this.openTrackCreatedDialog(data)
        }, error => {
          this.isError = true;
        });
    }
  };

  openTrackCreatedDialog(response : any) : void {
    const dialogRef = this.dialog.open(CreateTrackDialog, {
      width: '470px',
      data : response
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  };
}
