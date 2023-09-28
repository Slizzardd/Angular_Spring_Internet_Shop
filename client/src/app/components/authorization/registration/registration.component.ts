import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<RegistrationComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {}

  form = new FormGroup({
    email: new FormControl<string>(this.data?.email || null, [
      Validators.required,
      this.emailValidator
    ]),
    phoneNumber: new FormControl<string>(this.data?.phoneNumber || null, [
      Validators.required,
      this.phoneNumberValidator
    ]),
    firstName: new FormControl<string>(this.data?.firstName || null, [
      Validators.required
    ]),
    lastName: new FormControl<string>(this.data?.lastName || null, [
      Validators.required
    ]),
    password: new FormControl<string>('', [
      Validators.required,
      Validators.minLength(8)
    ]),
    password2: new FormControl<string>('', [
      Validators.required,
      Validators.minLength(8)
    ])
  });

  error: string = '';

  ngOnInit(): void {}

  onNoClick(): void {
    this.dialogRef.close(null);
  }

  onSubmit(): void {
    if (this.form.valid) {
      if (!this.passwordMatchValidator()) {
        this.error = 'The passwords must match';
        return;
      }

      this.data = {
        firstName: this.form.value.firstName,
        lastName: this.form.value.lastName,
        phoneNumber: this.form.value.phoneNumber,
        email: this.form.value.email,
        password: this.form.value.password,
      };

      this.dialogRef.close(this.data);
    } else {
      this.error = 'Not all fields are correctly filled in';
    }
  }

  passwordMatchValidator(): boolean {
    const password = this.form.value.password as string;
    const password2 = this.form.value.password2 as string;
    return password === password2;
  }

  emailValidator(control: FormControl): { [key: string]: any } | null {
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailPattern.test(control.value) ? null : { invalidEmail: true };
  }

  phoneNumberValidator(control: FormControl): { [key: string]: any } | null {
    const phonePattern = /^\+\d{1,4}\d{3,}$/;
    return phonePattern.test(control.value) ? null : { invalidPhoneNumber: true };
  }
}
