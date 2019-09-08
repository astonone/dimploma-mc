import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { User } from '../../../dto/user';
import { Dialog } from '../../../dto/dialog';
import { DialogService } from '../../../services/dialog.service';
import { SharedService } from '../../../services/shared.service';
import { Router} from '@angular/router';

@Component({
    selector: 'create-dialog',
    templateUrl: 'create-dialog.html',
    styleUrls: ['../dialogs.component.css']
})
export class CreateDialog {

    public title = '';
    public description = '';
    public myFriends: User[] = [];
    public createdDialog: Dialog = Dialog.createEmptyDialog();
    public dialogName: '';
    public error = false;

    constructor(
        private dialogService: DialogService,
        public dialogRef: MatDialogRef<CreateDialog>,
        public shared: SharedService,
        private router: Router,
        @Inject(MAT_DIALOG_DATA) public data: any) {
        this.title = data.title;
        this.description = data.description;
        this.myFriends = data.friends;
    }

    public onYesClick(): void {
        this.dialogRef.close();
        this.router.navigate(['dialog/' + this.createdDialog.id]);
    }

    public showUserInfo(user: User) {
        const firstName = user.userDetails.firstName === null ? '' : user.userDetails.firstName;
        const lastName = user.userDetails.lastName === null ? '' : user.userDetails.lastName;
        return firstName === '' && lastName === '' ? user.email : firstName + ' ' + lastName;
    }

    addUserToDialog(user: User) {
        if (this.dialogName.trim() === '') {
            this.error = true;
            return;
        }
        if (this.createdDialog.id === -1) {
            this.dialogService.createDialog(this.dialogName, this.shared.getLoggedUser().id).subscribe(data => {
               this.error = false;
               this.createdDialog = new Dialog(data);
               this.dialogService.addUserToDialog(this.createdDialog.id, user.id).subscribe(newData => {
                   this.createdDialog = new Dialog(newData);
               });
            });
        } else {
            this.dialogService.addUserToDialog(this.createdDialog.id, user.id).subscribe(data => {
                this.error = false;
                this.createdDialog = new Dialog(data);
            });
        }
    }
}
