import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {IOffer} from "../models/offer";
import {Observable, of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class OffersService {
  private baseUrl = 'http://localhost:8080/offers/';

  constructor(private httpClient: HttpClient) { }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem("auth-token")}`
    });
  }

  private getOptions(): { headers: HttpHeaders } {
    return { headers: this.getHeaders() };
  }
  createOffer(offer: any){

    const options = { headers: this.getHeaders() };

    return this.httpClient.post<IOffer>(`${this.baseUrl}createOffer`, offer, options);
  }

  statusPayment(offer: IOffer){
    const options = { headers: this.getHeaders() };

    return this.httpClient.post<string>(`${this.baseUrl}startPayment`, offer, options)
  }

  findOfferByLinkForPayment(linkForPayment: string): Observable<IOffer>{
    return this.httpClient.get<IOffer>(`${this.baseUrl}getOfferByLinkForPayment/${linkForPayment}`)
  }

  successfulPayment(offer: IOffer){
    const options = { headers: this.getHeaders() };

    return this.httpClient.post<IOffer>(`${this.baseUrl}successfulPayment`, offer, options)
  }


}
