import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { catchError, map } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import { IProducts } from '../models/products';
import { ProductsService } from './products.service';

@Injectable({
  providedIn: 'root'
})
export class ProductsResolver implements Resolve<IProducts> {

  constructor(private productService: ProductsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IProducts> {
    const productId = route.params?.['id'];

    if (!productId) {
      return of({} as IProducts);
    }

    return this.productService.findProduct(productId).pipe(
      catchError(() => {
        this.router.navigate(['products']);
        return of({} as IProducts);
      })
    );
  }
}
