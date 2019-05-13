import {Component, OnDestroy, OnInit} from '@angular/core';
import { SharedService } from '../../services/shared.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DialogService } from '../../services/dialog.service';
import {Dialog} from '../../dto/dialog';
import {Message} from '../../dto/message';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.css']
})
export class DialogComponent implements OnInit, OnDestroy {

  private dialog: Dialog;
  public messages: Message[] = [];
  public text: '';
  private interval: any;
  private audio: any;

  constructor(private shared: SharedService,
              private router: Router,
              private route: ActivatedRoute,
              private dialogService: DialogService) { }

  ngOnInit() {
    if (this.shared.getLoggedUser() === null) {
      this.router.navigate(['music']);
    }
    const dialogId = this.route.snapshot.paramMap.get('id');
    this.dialogService.getDialog(Number(dialogId)).subscribe(data => {
      this.dialog = new Dialog(data);
      this.messages = this.dialog.messages;
    });

    this.audio = new Audio();
    this.audio.src = '../../../assets/sound/message.mp3';
    this.audio.load();

    this.interval = setInterval(() => {
      this.dialogService.getDialog(Number(dialogId)).subscribe(data => {
        this.dialog = new Dialog(data);
        if (this.messages.length !== this.dialog.messages.length) {
          this.messages = this.dialog.messages;
          this.playMessageSound();
        }
      });
    }, 15000);
  }

  ngOnDestroy() {
    clearInterval(this.interval);
  }

  getUserById(message: Message) {
    if (this.dialog.users.length !== 0) {
      const index = this.dialog.users.map(x => {
        return x.id;
      }).indexOf(message.userId);

      return this.dialog.users[index].userDetails.firstName + ' ' + this.dialog.users[index].userDetails.lastName;
    } else {
      return '';
    }
  }

  sendMessage() {
    this.dialogService.addMessageToDialog(this.dialog.id, this.shared.getLoggedUser().id, this.text).subscribe(data => {
      this.text = '';
      this.dialog = new Dialog(data);
      if (this.messages.length !== this.dialog.messages.length) {
        this.messages = this.dialog.messages;
        this.playMessageSound();
      }
    });
  }

  playMessageSound() {
    this.audio.play();
  }
}
