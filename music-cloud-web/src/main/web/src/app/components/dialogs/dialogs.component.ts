import { Component, OnInit } from '@angular/core';
import { Dialog } from '../../dto/dialog';
import { DialogService } from '../../services/dialog.service';
import { SharedService } from '../../services/shared.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DialogList } from '../../dto/dialog-list';

@Component({
  selector: 'app-dialogs',
  templateUrl: './dialogs.component.html',
  styleUrls: ['./dialogs.component.css']
})
export class DialogsComponent implements OnInit {

  public dialogs: Dialog[] = [];
  private dialogList: DialogList;

  constructor(private dialogService: DialogService,
              private shared: SharedService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit() {
    if (this.shared.getLoggedUser() === null) {
      this.router.navigate(['music']);
    }
    const userId = this.route.snapshot.paramMap.get('id');
    this.dialogService.getUserDialogs(Number(userId)).subscribe(data => {
        this.dialogList = new DialogList(data);
        this.dialogs = this.dialogList.dialogs;
    });
  }

  public openDialog(dialogId: number) {
    this.router.navigate(['dialog/' + dialogId]);
  }
}
