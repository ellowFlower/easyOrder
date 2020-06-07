import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import {OrderPay} from '../../dtos/orderPay';
import {OrderFindAll} from '../../dtos/orderFindAll';

@Component({
  selector: 'app-order-pay-detail',
  templateUrl: './order-pay-detail.component.html',
  styleUrls: ['./order-pay-detail.component.scss']
})
export class OrderPayDetailComponent implements OnInit {
  @Input() orderPay: OrderPay;
  @Output() myEventErledigtMultiple = new EventEmitter();
  constructor() { }

  ngOnInit() {
  }

  /**
   * call method from parent component to change the status for all orders of one table to ERLEDIGT
   */
  callParentToChangeStatusToErledigtForMultipleOrders(order: OrderPay) {
    this.myEventErledigtMultiple.emit(order);
  }

}
