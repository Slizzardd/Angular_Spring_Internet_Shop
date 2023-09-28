import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {BaseComponent} from "./components/base/base.component";
import {ProductsComponent} from "./components/products/products.component";
import {ProductDetailsComponent} from "./components/product-details/product-details.component";
import {BasketComponent} from "./components/basket/basket.component";
import {ProductsResolver} from "./services/products.resolver";
import {PaymentComponent} from "./components/payment/payment.component";




const routes: Routes = [
  {path: '', component:BaseComponent},
  {path: 'products', component:ProductsComponent},
  {path: 'product/:id', component:ProductDetailsComponent, resolve: {data: ProductsResolver}},
  {path: 'basket/:id', component:BasketComponent},
  {path: 'offers/payment/:uniqLink', component:PaymentComponent},

  {path: "**", redirectTo: "", component: BaseComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
