import { Component, OnInit } from '@angular/core';
import {Category} from '../../dtos/category';
import {CategoryService} from '../../services/category.service';

@Component({
  selector: 'app-menucard',
  templateUrl: './menucard.component.html',
  styleUrls: ['./menucard.component.scss']
})
export class MenucardComponent implements OnInit {
  foodCategories: Category[];
  drinkCategories: Category[];

  success = false;
  error = false;
  errorMessage = '';
  constructor(private categoryService: CategoryService) { }

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
}
