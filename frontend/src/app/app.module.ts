import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {NgbDateParserFormatter, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';

import {InputTextModule} from 'primeng/inputtext';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {FoodComponent} from './components/food/food.component';
import {CardModule} from 'primeng/card';
import {ButtonModule} from 'primeng/button';
import {DialogModule} from 'primeng/dialog';
import {BrowserAnimationsModule, NoopAnimationsModule} from '@angular/platform-browser/animations';
import { FlexLayoutModule } from '@angular/flex-layout';
import { RegistertableComponent } from './components/registertable/registertable.component';
import { OrderComponent } from './components/order/order.component';
import { OrderDetailComponent } from './components/order-detail/order-detail.component';
import { CategoryComponent } from './components/category/category.component';
import { DrinkComponent } from './components/drink/drink.component';
import { CartComponent } from './components/cart/cart.component';
import {MatCardModule, MatDividerModule, MatSliderModule} from '@angular/material';
import { FoodCartComponent } from './components/food-cart/food-cart.component';
import {MatDialogModule} from '@angular/material/dialog';

import {MatTabsModule} from '@angular/material';

import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';
import { MenucardComponent } from './components/menucard/menucard.component';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import {NgbDateCustomParserFormatter, StatisticComponent} from './components/statistic/statistic.component';
import { OrderPayDetailComponent } from './components/order-pay-detail/order-pay-detail.component';

@NgModule
({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    RegistertableComponent,
    OrderComponent,
    OrderDetailComponent,
    FoodComponent,
    RegistertableComponent,
    CategoryComponent,
    RegistertableComponent,
    CategoryComponent,
    DrinkComponent,
    MenucardComponent,
    CartComponent,
    FoodCartComponent,
    StatisticComponent,
    OrderPayDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    InputTextModule,
    InputTextareaModule,
    CardModule,
    ButtonModule,
    DialogModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    MatTabsModule,
    NgMultiSelectDropDownModule.forRoot(),
    MatCardModule,
    MatDividerModule,
    MatDialogModule,
    NgxChartsModule
  ],
  providers: [
    httpInterceptorProviders],
    bootstrap: [AppComponent]

})
export class AppModule {
}

