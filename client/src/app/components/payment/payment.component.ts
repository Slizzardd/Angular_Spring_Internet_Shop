import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {OffersService} from "../../services/offers.service";
import {IOffer} from "../../models/offer";

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent {

  constructor(private offerService: OffersService, private route: ActivatedRoute) {
    this.offerService.findOfferByLinkForPayment(this.route.snapshot.params['uniqLink']).subscribe(data =>{
      this.offer = data;
    })
  }
  offer: IOffer;
  successfulPayment() {
    this.offerService.successfulPayment(this.offer).subscribe(data => {

    })
  }


}
