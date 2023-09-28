import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {IProducts} from "../../models/products";

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit{

  product: IProducts;

  constructor(private route: ActivatedRoute) {
  }
  ngOnInit(): void {
    this.route.data.subscribe((data) =>{
      this.product = data['data'];
    })
  }

}
