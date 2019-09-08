import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { Dialog } from '../../dto/dialog';
import { DialogService } from '../../services/dialog.service';
import { SharedService } from '../../services/shared.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DialogList } from '../../dto/dialog-list';
import { MatDialog } from '@angular/material';
import { CreateDialog } from './dialog/create-dialog';
import { UserList } from '../../dto/user-list';
import { User } from '../../dto/user';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-dialogs',
  templateUrl: './dialogs.component.html',
  styleUrls: ['./dialogs.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DialogsComponent implements OnInit {

  public dialogs: Dialog[] = [];
  private dialogList: DialogList;
  private myFriends: User[] = [];

  constructor(private dialogService: DialogService,
              private shared: SharedService,
              private route: ActivatedRoute,
              private router: Router,
              public dialog: MatDialog,
              private userService: UserService,
              private cdRef: ChangeDetectorRef) { }

  ngOnInit() {
    if (this.shared.getLoggedUser() === null) {
      this.router.navigate(['music']);
    }
    this.loadFriends();
    this.loadDialogs();
  }

  private loadDialogs() {
    const userId = this.route.snapshot.paramMap.get('id');
    this.dialogService.getUserDialogs(Number(userId)).subscribe(data => {
      this.dialogList = new DialogList(data);
      this.dialogs = this.dialogList.dialogs;
      this.cdRef.detectChanges();
    });
  }

  public openDialog(dialogId: number) {
    this.router.navigate(['dialog/' + dialogId]);
  }

  createDialog() {
    this.openCreateDialogDialog({title: 'Создание диалога',
      description: 'Здесь вы сможете добавить своих друзей в чат', friends: this.myFriends});
  }

  private openCreateDialogDialog(data: any): void {
    const dialogRef = this.dialog.open(CreateDialog, {
      data : data
    });
    dialogRef.afterClosed().subscribe(result => {
      this.loadDialogs();
    });
  }

  private loadFriends() {
    this.myFriends = [];
    this.userService.getAllFriends(this.shared.getLoggedUser().id)
        .subscribe(data => {
          const responseFriends = new UserList(data);
          responseFriends.users.forEach(user => {
            this.myFriends.push(new User(user));
          });
        });
  }
}
