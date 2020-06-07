import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Order} from '../../dtos/order';
import {OrderFindAll} from '../../dtos/orderFindAll';


@Component({
  selector: 'app-order-detail',
  templateUrl: './order-detail.component.html',
  styleUrls: ['./order-detail.component.scss']
})
export class OrderDetailComponent implements OnInit {
  @Input() order: Order;
  @Output() myEventServieren = new EventEmitter();
  @Output() myEventErledigt = new EventEmitter();
  @Output() myEventServiert = new EventEmitter();
  isShow = false;
  constructor() { }

  ngOnInit() {
  }

  toggleDisplay() {
    this.isShow = !this.isShow;
  }

  /**
   * Toggle Display and call method from parent component to change the status to SERVIEREN
   */
  callParentToChangeStatusToServieren(order: OrderFindAll) {
    this.toggleDisplay();
    this.myEventServieren.emit(order);
  }

  /**
   * Toggle Display and call method from parent component to change the status to ERLEDIGT
   */
  callParentToChangeStatusToErledigt(order: OrderFindAll) {
    this.toggleDisplay();
    this.myEventErledigt.emit(order);
  }

  /**
   * Toggle Display and call method from parent component to change the status to SERVIERT
   */
  callParentToChangeStatusToServiert(order: OrderFindAll) {
    this.toggleDisplay();
    this.myEventServiert.emit(order);
  }
}
