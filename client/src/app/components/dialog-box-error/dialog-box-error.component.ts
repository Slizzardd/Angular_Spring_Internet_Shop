import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-dialog-box-error',
  templateUrl: './dialog-box-error.component.html',
  styleUrls: ['./dialog-box-error.component.css']
})
export class DialogBoxErrorComponent {

  constructor(
    public dialogRef: MatDialogRef<DialogBoxErrorComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
  }
}
