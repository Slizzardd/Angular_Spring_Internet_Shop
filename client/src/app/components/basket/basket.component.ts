import {Component, OnInit} from '@angular/core';
import {BasketService} from "../../services/basket.service";
import {ActivatedRoute, Router} from "@angular/router";
import {IBasket} from "../../models/basket";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {DialogBoxErrorComponent} from "../dialog-box-error/dialog-box-error.component";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {IUser} from "../../models/user";
import {UserSharedService} from "../../services/shared/user.shared";
import {OffersService} from "../../services/offers.service";
import {IOffer} from "../../models/offer";

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.css']
})
export class BasketComponent implements OnInit {
  constructor(
    private userShared: UserSharedService,
    private basketService: BasketService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private offerService: OffersService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.userId = this.route.snapshot.params['id']
    this.user = this.userShared.getSharedEntity();
    this.canEdit = this.user?.role.toLowerCase() === 'admin';

    this.myForm = new FormGroup({
      productId: new FormControl('', Validators.required),
    });


    this.loadBasket()
  }

  totalAmount: number = 0;
  userId: number;
  baskets: IBasket[];
  user: IUser;
  canEdit: boolean = false;
  myForm: FormGroup;

  offer: IOffer;

  loadBasket() {
    if(localStorage.getItem('auth-token') == null){
      this.router.navigate(['/']);
      return;
    }
    this.basketService.findAllProductInBasketByUserId(this.userId).subscribe((data) => {
      this.baskets = data;
      this.getTotalAmount()
    }, error => {
      if (error.status === 403) {
        this.openDialog("You not have permission for this data")
      }
    });
  }

  getTotalAmount() {
    this.totalAmount = 0;
    for (let i = 0; i < this.baskets.length; i++) {
      this.totalAmount += this.baskets[i].product.price * this.baskets[i].quantityProductOnBasket;
    }
  }

  async addProduct(id: number) {
    try {
      await this.basketService.addProductToBasket(id, this.userId).toPromise();
      const basketToUpdate = this.baskets.find(basket => basket.product.id === id);
      if (basketToUpdate) {
        basketToUpdate.quantityProductOnBasket += 1;
        this.getTotalAmount();
        this.openDialog('The product has been added to your cart');
      }
    } catch (error) {
      this.openDialog('Failed to add the product to your cart');
    }
  }

  async deleteOneProduct(id: number) {
    try {
      await this.basketService.deleteOneProductFromUserBasket(id, this.userId).toPromise();
      const basketToUpdate = this.baskets.find(basket => basket.product.id === id);
      if (basketToUpdate) {
        basketToUpdate.quantityProductOnBasket -= 1;
        if (basketToUpdate.quantityProductOnBasket <= 0) {
          this.baskets = this.baskets.filter(basket => basket.product.id !== id);
        }
        this.getTotalAmount();
        this.openDialog('The product has been deleted from your cart');
      }
    } catch (error) {
      this.openDialog('Failed to delete the product from your cart');
    }
  }

  async deleteAllProduct(id: number) {
    try {
      await this.basketService.deleteAllProductFromUserBasket(id, this.userId).toPromise();
      this.baskets = this.baskets.filter(basket => basket.product.id !== id);
      this.getTotalAmount();
    } catch (error) {
      this.openDialog('Failed to delete all products from your cart');
    }
  }

  openDialog(text: string) {
    const errorDialogConfig = new MatDialogConfig();
    errorDialogConfig.width = '500px';
    errorDialogConfig.data = {
      text: text
    };
    this.dialog.open(DialogBoxErrorComponent, errorDialogConfig);
  }

  async addProductById() {
    if (this.myForm.valid) {
      const productId = this.myForm.value.productId;
      try {
        await this.basketService.addProductToBasket(productId, this.userId).toPromise();
        this.openDialog('Product is added');
        location.reload()
      } catch (error) {
        this.openDialog('There is no product with this id');
      }
    }
  }


  createOffer() {
    let offer = {
      address: 'Ahsarova 15',
      userId: this.userId
    }
    this.offerService.createOffer(offer).subscribe(data => {
      this.offer = data;
      this.offer.userId = this.userId;
      this.router.navigate([`/offers/payment/${this.offer.linkForPayment}`]);
      this.offerService.statusPayment(this.offer).subscribe(data => {

      })
    }, error => {
      this.openDialog('Log in');
    });
  }
}
