import { Injectable } from '@angular/core';
import {OrderFindAll} from '../dtos/orderFindAll';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Globals} from '../global/globals';
import {OrderPay} from '../dtos/orderPay';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private authBaseUri: string = this.globals.backendUri + '/orders';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Load all orders without status ERLEDIGT
   */
  getOrder(): Observable<OrderFindAll[]> {
    return this.httpClient.get<OrderFindAll[]>(this.authBaseUri);
  }


  /**
   * Load all orders with status NEU
   */
  getOrderNeu(): Observable<OrderFindAll[]> {
    return this.httpClient.get<OrderFindAll[]>(this.authBaseUri + '/new');
  }

  /**
   * Load all to orders with status SERVIEREN
   */
  getOrderServieren(): Observable<OrderFindAll[]> {
    return this.httpClient.get<OrderFindAll[]>(this.authBaseUri + '/serve');
  }

  /**
   * Loads all orders without status ERLEDIGT
   */
  getOrderPay(): Observable<OrderPay[]> {
    return this.httpClient.get<OrderPay[]>(this.authBaseUri + '/pay');
  }


  /**
   * Update Status of order specific order
   */
  update(order: OrderFindAll): Observable<{}> {
    return this.httpClient.patch(this.authBaseUri + '?id=' + order.id + '&status=' + order.status, null, this.httpOptions);
  }

  /**
   * Update Status of all orders from one table to ERLEDIGT
   */
  updateStatusAll(orderPay: OrderPay): Observable<{}> {
    return this.httpClient.patch(this.authBaseUri + '/pay' + '?tableId=' + orderPay.tableId, null, this.httpOptions);
  }

}
