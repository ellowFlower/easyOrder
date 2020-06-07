import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {RegisterComponent} from './components/register/register.component';

import {FoodComponent} from './components/food/food.component';
import {DrinkComponent} from './components/drink/drink.component';
import {RegistertableComponent} from './components/registertable/registertable.component';
import {CategoryComponent} from './components/category/category.component';
import {OrderComponent} from './components/order/order.component';
import {MenucardComponent} from './components/menucard/menucard.component';
import {FoodCartComponent} from './components/food-cart/food-cart.component';
import {StatisticComponent} from './components/statistic/statistic.component';
const routes: Routes = [
  {path: '', canActivate: [AuthGuard], component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  // {path: 'listFood', component: ListFoodComponent},
  {path: 'drink', component: DrinkComponent},
  {path: 'food', component: FoodComponent},
  {path: 'registertable', component: RegistertableComponent},
  {path: 'order', component: OrderComponent},
  // {path: 'createFood', component: CreateFoodComponent},
  {path: 'category', component: CategoryComponent},
  // {path: 'menucard', component: MenucardComponent},
  {path: 'menu', component: FoodCartComponent},
  {path: 'statistic', component: StatisticComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
