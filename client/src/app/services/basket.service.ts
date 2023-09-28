import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IBasket } from '../models/basket';

@Injectable({
  providedIn: 'root'
})
export class BasketService {
  private baseUrl = 'http://localhost:8080/basket/';

  constructor(private httpClient: HttpClient) { }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem("auth-token")}`,
      'Content-Type': 'application/json'
    });
  }

  private getOptions(): { headers: HttpHeaders } {
    return { headers: this.getHeaders() };
  }

  addProductToBasket(productId: number, userId: number): Observable<string> {
    const options = { headers: this.getHeaders() };

    return this.httpClient.post<string>(`${this.baseUrl}addProductToBasket`,
      JSON.stringify({productId: productId, userId: userId}), options);
  }

  deleteOneProductFromUserBasket(productId: number, userId: number): Observable<string> {
    const options = { headers: this.getHeaders() };

    return this.httpClient.post<string>(`${this.baseUrl}deleteOneProductFromBasket`,
      JSON.stringify({productId: productId, userId: userId}), options);
  }

  deleteAllProductFromUserBasket(productId: number, userId: number): Observable<string> {
    const options = { headers: this.getHeaders() };

    return this.httpClient.post<string>(`${this.baseUrl}deleteAllProductFromBasket`,
      JSON.stringify({productId: productId, userId: userId}), options);
  }

  findAllProductInBasketByUserId(userId: number): Observable<IBasket[]> {
    const options = this.getOptions();

    return this.httpClient.get<IBasket[]>(`${this.baseUrl}findAllProductByUserBasket/${userId}`, options);
  }

}
