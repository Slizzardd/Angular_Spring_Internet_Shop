import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UsersService } from '../../../services/users.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  error: string = '';

  form: FormGroup = new FormGroup({
    login: new FormControl('', [
      Validators.required,
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
    ])
  });

  constructor(
    public dialogRef: MatDialogRef<LoginComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  onNoClick(): void {
    this.dialogRef.close(null);
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.data = {
        email: this.form.value.login,
        password: this.form.value.password
      };
      this.dialogRef.close(this.data);
    } else {
      this.error = 'Not all fields are correctly filled in';
    }
  }
}
