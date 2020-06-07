import {ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Food} from '../../dtos/food';
import {Order} from '../../dtos/order';
import {Drink} from '../../dtos/drink';
import {CartService} from '../../services/cart.service';
import Swal from 'sweetalert2';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

  foods: Food[] = [];
  drinks: Drink[] = [];

  foods1: Food[] = [];
  drinks1: Drink[] = [];

  foods_id: number[] = [];
  drinks_id: number[] = [];

  assistance: string = '';
  index: number = 0;
  sum: number = 0;
  isEmpt: boolean = true;

  order1: Order = new Order(null, null, null, null, null, null, null, null);
  orderBoole: boolean = false;

  cartEmp: boolean = false;

  constructor(private cartService: CartService, private authService: AuthService) { }

  ngOnInit() {
  }

  addFoodToCart(f: Food): void {
    this.foods.push(f);
    this.sum = (this.sum + f.price);
    this.sum.toFixed(2);
  }

  addDrinkToCart(d: Drink): void {
    this.drinks.push(d);
    this.sum = this.sum + d.price;
    this.sum.toFixed(2);
  }

  deleteFoodFromCart(food: Food): void {
    this.sum = this.sum - food.price;
    this.sum.toFixed(2);
    this.foods.splice(this.foods.indexOf(food), 1);
  }

  deleteDrinkFromCart(drink: Drink): void {
    this.sum = this.sum - drink.price;
    this.sum.toFixed(2);
    this.drinks.splice(this.drinks.indexOf(drink), 1);
  }

  isOrdered(): boolean {
    return this.orderBoole;
  }

  isEmpty(): boolean {
    if (this.foods.length !== 0 || this.drinks.length !== 0) {
      this.isEmpt = false;
    } else {
      this.isEmpt = true;
    }
    return this.isEmpt;
  }

  order(): void {
    if (this.foods.length === 0 && this.drinks.length === 0) {
      this.cartEmp = true;
    } else if (this.foods.length !== 0 || this.drinks.length !== 0) {
      this.cartEmp = false;
    }

    if (this.cartEmp === false) {
      for (const food of this.foods) {
        this.foods_id[this.index] = food.id;
        this.index = this.index + 1;
      }
      for (const drink of this.drinks) {
        this.drinks_id[this.index] = drink.id;
        this.index = this.index + 1;
      }

      console.log('Cart Component Order: Success 1');

      const order: Order = new Order(null, 'NEU', this.assistance, null, null, this.foods_id, this.drinks_id, this.authService.getTokenUserId());
      console.log(order);

      this.cartService.order(order).subscribe(
        () => {
          this.foods.length = 0;
          this.drinks.length = 0;
          this.foods_id.length = 0;
          this.drinks_id.length = 0;
          this.index = 0;
          this.assistance = '';
          console.log(this.foods);
          this.defaultServiceSuccessHandling('Bestellung erfolgreich aufgegeben');
        },
        error => {
          this.defaultServiceErrorHandling(error, 'Bestellung konnte nicht aufgegeben werden');
        }
      );
    } else {
      this.cartEmptyErrorHandling('Leider ist ihr Warenkorb leer');
    }

    const order: Order = new Order(null, 'NEU', this.assistance, null, null, this.foods_id, this.drinks_id, this.authService.getTokenUserId());
    console.log(order);
    this.orderBoole = true;
    this.order1 = order;

    this.foods1 = JSON.parse(JSON.stringify(this.foods));
    this.drinks1 = JSON.parse(JSON.stringify(this.drinks));
    this.assistance = '';
  }

  orderHelp(string: string) {


    const orderHelp: Order = new Order(null, 'NEU', string, null, null, [], [], this.authService.getTokenUserId());

    this.cartService.order(orderHelp).subscribe(
      () => {
        this.orderBoole = true;
        this.foods.length = 0;
        this.drinks.length = 0;
        this.foods_id.length = 0;
        this.drinks_id.length = 0;
        this.index = 0;
        this.assistance = null;
        console.log(this.foods);
        this.defaultServiceSuccessHandling('Ein Kellner ist in kürze bei Ihnen!');
      },
      error => {
        this.defaultServiceErrorHandling(error, 'Leider ist etwas schiefgegangen!');
      }
    );

  }

  pay() {
    this.orderHelp('Bitte Zahlen');
  }

  needCutlery() {
    this.orderHelp('Bitte um Besteck');
  }

  random() {
    this.orderHelp('Bitte um Hilfe');
  }

  private cartEmptyErrorHandling (errorText: string) {
    Swal.fire(
      'Es gab ein Problem!',
      errorText,
      'question'
    );
  }

  private defaultServiceErrorHandling (error: any, errorText: string) {
    console.log(error);
    Swal.fire(
      'Es gab ein Problem!',
      errorText,
      'question'
    );
  }

  private defaultServiceSuccessHandling(successText: string) {
    Swal.fire({
      title: 'Wunderbar!',
      text: successText
    });

    console.log('Order success');
  }
}
