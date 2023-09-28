import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IProducts } from '../models/products';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {
  private baseUrl = 'http://localhost:8080/';

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem("auth-token")}`
    });
  }

  private getOptions(): { headers: HttpHeaders } {
    return { headers: this.getHeaders() };
  }

  findAllProducts(): Observable<IProducts[]> {
    return this.http.get<IProducts[]>(`${this.baseUrl}products/findAll`);
  }

  findProduct(id: number): Observable<IProducts> {
    return this.http.get<IProducts>(`${this.baseUrl}products/product/${id}`);
  }

  createProduct(product: IProducts): Observable<IProducts> {
    const options = this.getOptions();

    return this.http.post<IProducts>(`${this.baseUrl}products/create`, product, options);
  }

  deleteProduct(id: number): Observable<any> {
    const headers = this.getHeaders().append('productId', id.toString());
    const options = { headers: headers };

    return this.http.delete<any>(`${this.baseUrl}products/delete`, options);
  }

  updateProduct(product: IProducts): Observable<IProducts> {
    const options = this.getOptions();

    return this.http.put<IProducts>(`${this.baseUrl}products/update`, product, options);
  }
}
