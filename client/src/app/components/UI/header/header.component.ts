import {Component} from '@angular/core';
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {RegistrationComponent} from "../../authorization/registration/registration.component";
import {UsersService} from "../../../services/users.service";
import {IUser} from "../../../models/user";
import {LoginComponent} from "../../authorization/login/login.component";
import {DialogBoxErrorComponent} from "../../dialog-box-error/dialog-box-error.component";
import {Router} from "@angular/router";
import {UserSharedService} from "../../../services/shared/user.shared";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  user: IUser;
  isLogin: boolean = false;

  constructor(
    public dialog: MatDialog,
    private userService: UsersService,
    private userShared: UserSharedService,
    private router: Router
  ) {
    this.user = userShared.getSharedEntity();
    if (this.user) {
      this.isLogin = true;
    }
  }

  registration() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '500px';
    dialogConfig.disableClose = true;
    const dialogRef = this.openDialog(RegistrationComponent, {}, dialogConfig);

    dialogRef.afterClosed().subscribe((data) => {
      if (data) {
        this.createUser(data);
      }
    });
  }

  login() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '500px';
    dialogConfig.disableClose = true;
    const dialogRef = this.openDialog(LoginComponent, {}, dialogConfig);

    dialogRef.afterClosed().subscribe((data) => {
      if (data) {
        this.loginUser(data);
      }
    });
  }

  createUser(user: IUser) {
    this.userService.createUser(user).subscribe(() => {
      this.openSuccessDialog("Account created");
    }, error => {
      if (error.status === 409) {
        this.openErrorDialog("A user with an email/phone number already exists")
      } else {
        this.openErrorDialog("There was an error, try again");
      }
    });
  }

  logout() {
    this.userShared.clearSharedEntity();
    localStorage.removeItem('auth-token');
    location.reload();
  }

  loginUser(user: IUser) {
    this.userService.loginUser(user).subscribe(() => {
      this.getActualUser();
      this.isLogin = true;
      location.reload();
    }, error => {
      if (error.status === 401) {
        this.openErrorDialog("Incorrect data entered, try again")
      }
    });
  }

  getActualUser() {
    this.userService.getActualUser().subscribe((data) => {
      this.userShared.setSharedEntity(data);
    });
  }

  goToBasket(id: number) {
    this.router.navigate(['basket', id]);
  }

  private openDialog(component: any, data: any, config: MatDialogConfig) {
    config.data = data;
    return this.dialog.open(component, config);
  }

  private openErrorDialog(text: string) {
    this.openDialog(DialogBoxErrorComponent, {text}, {width: '500px'});
  }

  private openSuccessDialog(text: string) {
    this.openDialog(DialogBoxErrorComponent, {text}, {width: '500px'});
  }
}
