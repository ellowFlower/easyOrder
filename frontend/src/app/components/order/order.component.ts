import {Component, OnInit} from '@angular/core';
import {OrderService} from '../../services/order.service';
import {OrderFindAll} from '../../dtos/orderFindAll';
import {OrderPay} from '../../dtos/orderPay';
import {interval} from 'rxjs';

@Component({
  selector: 'app-orders',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})

export class OrderComponent implements OnInit {

  constructor(private orderService: OrderService) {

  }

  error: boolean = false;
  errorMessage: string = '';

  private orderFindAll: OrderFindAll[];
  private helpList: OrderFindAll[];
  selectedOrder: OrderFindAll;

  private orderFindNeu: OrderFindAll[];
  private orderFindServieren: OrderFindAll[];
  private orderFindPay: OrderPay[];
  selectedOrderNeu: OrderFindAll;
  selectedOrderServieren: OrderFindAll;
  selectedOrderPay: OrderPay;

  ngOnInit() {
    // this.loadOrderNeu();
    interval(4000).subscribe(x => {this.loadOrderNeu(); });
    interval(4000).subscribe(x => {this.loadOrderServieren(); });
    interval(4000).subscribe(x => {this.loadOrderPay(); });
  }

  /**
   * Select orders for tab Neu and unselect the others
   */
  onSelectNeu(orderFindAll: OrderFindAll) {
    this.selectedOrderNeu = orderFindAll;
    this.selectedOrderServieren = undefined;
    this.selectedOrderPay = undefined;
  }

  /**
   * Select orders for tab Zu servieren and unselect the others
   */
  onSelectServieren(orderFindAll: OrderFindAll) {
    this.selectedOrderServieren = orderFindAll;
    this.selectedOrderNeu = undefined;
    this.selectedOrderPay = undefined;
  }

  /**
   * Select orders for tab Zu bezahlen and unselect the others
   */
  onSelectPay(orderFindPay: OrderPay) {
    this.selectedOrderPay = orderFindPay;
    this.selectedOrderNeu = undefined;
    this.selectedOrderServieren = undefined;
  }

  /**
   * Unselect all orders
   */
  unSelectAll() {
    this.selectedOrderNeu = undefined;
    this.selectedOrderServieren = undefined;
    this.selectedOrderPay = undefined;
  }

  getHelp(): OrderFindAll[] {
    return this.helpList;
  }

  /**
   * Change status of order
   */
  changeStatus(status: string, order: OrderFindAll) {
    order.status = status;
    this.unSelectAll();
    this.orderService.update(order).subscribe(
      () => {
        this.loadOrderServieren();
        this.loadOrderNeu();
        this.loadOrderPay();
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Change status of all orders from one table
   */
  changeStatusAll(status: string, order: OrderPay) {
    this.unSelectAll();
    this.orderService.updateStatusAll(order).subscribe(
      () => {
        this.loadOrderServieren();
        this.loadOrderNeu();
        this.loadOrderPay();
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }


  private loadOrder() {
    this.orderService.getOrder().subscribe(
      (orderFindAll: OrderFindAll[]) => {
        console.log(orderFindAll);
        const oderList: OrderFindAll[] = [];
        const helpList: OrderFindAll[] = [];
        for (const i of orderFindAll) {
          if (i.assistance === 'Bitte Zahlen' || i.assistance === 'Bitte um Besteck' || i.assistance === 'Bitte um Hilfe') {
            helpList.push(i);
          } else {
            oderList.push(i);
          }
        }
        console.log(oderList);
        console.log(helpList);
        this.orderFindAll = oderList;
        this.helpList = helpList;
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }


  /**
   * Loads the specified page of orders with status NEU
   */
  private loadOrderNeu() {
    this.orderService.getOrderNeu().subscribe(
      (orderFindNeu: OrderFindAll[]) => {
        console.log(orderFindNeu);
        const oderList: OrderFindAll[] = [];
        const helpList: OrderFindAll[] = [];
        for (const i of orderFindNeu) {
          if (i.assistance === 'Bitte Zahlen' || i.assistance === 'Bitte um Besteck' || i.assistance === 'Bitte um Hilfe') {
            helpList.push(i);
          } else {
            oderList.push(i);
          }
        }
        console.log(oderList);
        console.log(helpList);
        this.helpList = helpList;
        this.orderFindNeu = oderList;
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Loads the specified page of orders with status not ERLEDIGT
   */
  private loadOrderPay() {
    this.orderService.getOrderPay().subscribe(
      (orderFindPay: OrderPay[]) => {
        const oderList: OrderFindAll[] = [];

        this.orderFindPay = orderFindPay;
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  /**
   * Loads the specified page of orders with status SERVIEREN
   */
  private loadOrderServieren() {
    this.orderService.getOrderServieren().subscribe(
      (orderFindServieren: OrderFindAll[]) => {
        this.orderFindServieren = orderFindServieren;
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  deleteHelpOrder(help: OrderFindAll) {
    this.helpList.splice(this.helpList.indexOf(help), 1);
    this.changeStatus('ERLEDIGT', help);
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }

}
