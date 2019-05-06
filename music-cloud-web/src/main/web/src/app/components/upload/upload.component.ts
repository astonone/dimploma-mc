import { Component, OnInit } from '@angular/core';
import { TrackService } from '../../services/track.service';
import { MatDialog } from '@angular/material';
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { FileService } from '../../services/file.service';
import { SharedService } from '../../services/shared.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-download',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {

  public isError: boolean;
  public response: any;
  public isLoading: boolean;
  public isSaving: boolean;
  public isSuccess: boolean;
  public selectedFiles: FileList;
  public currentFileUpload: File;
  public progress: { percentage: number } = { percentage: 0 };

  constructor(private trackService: TrackService,
              public dialog: MatDialog,
              private fileService: FileService,
              private shared: SharedService,
              private router: Router) { }

  ngOnInit() {
    if (this.shared.getLoggedUser() === null) {
      this.router.navigate(['login']);
    }
    this.isError = false;
  }

  public selectFile(event) {
    this.selectedFiles = event.target.files;
    this.isError = false;
    this.isSuccess = false;
  }

  public upload() {
    this.progress.percentage = 0;

    this.currentFileUpload = this.selectedFiles.item(0);
    this.fileService.pushAudioFileToStorage( this.currentFileUpload)
        .subscribe(event => {
          if (event.type === HttpEventType.UploadProgress) {
            this.progress.percentage = Math.round(100 * event.loaded / event.total);
            this.isLoading = true;
            if (this.progress.percentage === 100) {
              this.isLoading = false;
              this.isSaving = true;
            }
          } else if (event instanceof HttpResponse) {
            this.isError = false;
            this.isSuccess = true;
            this.isLoading = false;
            this.isSaving = false;
            this.response = event;
            this.trackService.addTrackToUser(this.shared.getLoggedUser().id, Number(JSON.parse(this.response.body).id)).subscribe();
          }
        }, error => {
          this.isError = true;
        });
    this.selectedFiles = undefined;
  }
}
