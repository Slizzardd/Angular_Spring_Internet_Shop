import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HeaderComponent} from './components/UI/header/header.component';
import {FooterComponent} from './components/UI/footer/footer.component';
import {ProductsComponent} from './components/products/products.component';
import {BasketComponent} from './components/basket/basket.component';
import {ProductDetailsComponent} from './components/product-details/product-details.component';
import {BaseComponent} from './components/base/base.component';
import {CommonModule} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AppRoutingModule} from "./app-routing.module";
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatDialogModule} from '@angular/material/dialog';
import { DialogBoxComponent } from './components/dialog-box/dialog-box.component';
import {MatInputModule} from '@angular/material/input';
import {MatMenuModule} from '@angular/material/menu';
import { LoginComponent } from './components/authorization/login/login.component';
import { RegistrationComponent } from './components/authorization/registration/registration.component';
import { DialogBoxErrorComponent } from './components/dialog-box-error/dialog-box-error.component';
import {MatSelectModule} from '@angular/material/select';
import {MatListModule} from '@angular/material/list';
import {MatTableModule} from '@angular/material/table';
import { PaymentComponent } from './components/payment/payment.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    ProductsComponent,
    BasketComponent,
    ProductDetailsComponent,
    BaseComponent,
    DialogBoxComponent,
    LoginComponent,
    RegistrationComponent,
    DialogBoxErrorComponent,
    PaymentComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    CommonModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatDialogModule,
    MatInputModule,
    MatMenuModule,
    MatSelectModule,
    MatListModule,
    MatTableModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
