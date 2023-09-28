import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormControl, FormGroup } from "@angular/forms";

@Component({
  selector: 'app-dialog-box',
  templateUrl: './dialog-box.component.html',
  styleUrls: ['./dialog-box.component.css']
})
export class DialogBoxComponent implements OnInit {
  isNew: boolean = true;

  myForm: FormGroup = new FormGroup({
    id: new FormControl(null),
    title: new FormControl(null),
    price: new FormControl(null),
    year: new FormControl(null),
    image: new FormControl('/assets/images/iphone15promax.png'),
    description: new FormControl(null),
    category: new FormControl(null),
    quantityInWarehouse: new FormControl(null),
  });

  constructor(
    public dialogRef: MatDialogRef<DialogBoxComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    if (this.data) {
      this.isNew = false;
      this.myForm.patchValue(this.data);
    }
  }

  ngOnInit(): void {}

  onNoClick(): void {
    this.dialogRef.close(null);
  }

  onSubmit() {
    this.dialogRef.close(this.myForm.value);
  }
}
