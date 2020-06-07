import {Component, OnInit, ViewChild} from '@angular/core';
import {CartComponent} from '../cart/cart.component';
import {Food} from '../../dtos/food';
import {CategoryService} from '../../services/category.service';
import {Category} from '../../dtos/category';
import {Drink} from '../../dtos/drink';

@Component({
  selector: 'app-food-cart',
  templateUrl: './food-cart.component.html',
  styleUrls: ['./food-cart.component.scss']
})
export class FoodCartComponent implements OnInit {
  @ViewChild(CartComponent, {static: false}) cart;

  constructor(private categoryService: CategoryService) { }

  foodCategories: Category[];
  drinkCategories: Category[];

  success = false;
  error = false;
  errorMessage = '';

  ngOnInit() {
    this.getAllFoodCategories();
    this.getAllDrinkCategories();
  }

  private getAllFoodCategories() {
    this.categoryService.getAllFoodCategories().subscribe(res => {
        this.foodCategories = res;
        this.vanishError();
        this.success = true;
      }, error => {
        this.vanishSuccess();
      }
    );
  }

  private getAllDrinkCategories() {
    this.categoryService.getAllDrinkCategories().subscribe(res => {
        this.drinkCategories = res;
        this.vanishError();
        this.success = true;
      }, error => {
        this.vanishSuccess();
      }
    );
  }

  vanishError() {
    this.error = false;
  }

  vanishSuccess() {
    this.success = false;
  }


  addFood(food: Food) {
    this.cart.addFoodToCart(food);
  }
  addDrink(drink: Drink) {
    this.cart.addDrinkToCart(drink);
  }
}
