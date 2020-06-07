import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Food} from '../dtos/food';
import {Observable} from 'rxjs';
import {Order} from '../dtos/order';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private authBaseUri: string = this.globals.backendUri + '/orders';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  order(order: Order): Observable<Order> {
    console.log('OK');
    console.log(this.authBaseUri);
    return this.httpClient.post<Order>(this.authBaseUri, order);
    console.log('OK');
  }
}

