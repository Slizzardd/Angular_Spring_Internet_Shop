  import { Component, OnInit } from '@angular/core';
import { IProducts } from '../../models/products';
import { ProductsService } from '../../services/products.service';
import { Router } from '@angular/router';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DialogBoxComponent } from '../dialog-box/dialog-box.component';
import { IUser } from '../../models/user';
import { UserSharedService } from '../../services/shared/user.shared';
import { BasketService } from '../../services/basket.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  products: IProducts[];
  user: IUser;
  canEdit: boolean = false;

  constructor(
    private productService: ProductsService,
    private router: Router,
    public dialog: MatDialog,
    private userShared: UserSharedService,
    private basketService: BasketService
  ) {}

  ngOnInit(): void {
    this.user = this.userShared.getSharedEntity();
    if (this.user && this.user.role.toLowerCase() === 'admin') {
      this.canEdit = true;
    }

    this.productService.findAllProducts().subscribe((data) => {
      this.products = data;
    });
  }

  openDialogForProduct(product?: IProducts): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '500px';
    dialogConfig.disableClose = true;
    dialogConfig.data = product;

    const dialogRef = this.dialog.open(DialogBoxComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((data) => {
      if (data) {
        if (data.id) {
          this.updateData(data);
        } else {
          this.postData(data);
        }
      }
    });
  }

  postData(product: IProducts) {
    this.productService.createProduct(product).subscribe((data) => {
      this.products.push(data);
    });
  }

  updateData(product: IProducts) {
    this.productService.updateProduct(product).subscribe((data) => {
      const index = this.products.findIndex((p) => p.id === data.id);

      if (index !== -1) {
        this.products[index] = data;
      }
    });
  }

  navigateToDescription(id: number) {
    this.router.navigate([`/product/${id}`]);
  }

  deleteItem(id: number) {
    this.productService.deleteProduct(id).subscribe(() => {
      this.products = this.products.filter((product) => product.id !== id);
    });
  }

  addProductToBasket(id: number) {
    if (this.user === null) {
      return; // Прерываем выполнение функции, если id равно null
    }
    this.basketService.addProductToBasket(id, this.user.id).subscribe(data => {
      const index = this.products.findIndex((p) => p.id === id);

      if (index !== -1) {
        this.products[index].quantityInWarehouse = this.products[index].quantityInWarehouse - 1;
      }
    }, error => {

    });
  }
}
