import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Food} from '../dtos/food';
import {Observable} from 'rxjs';
import {Category} from '../dtos/category';


@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private authBaseUri: string = this.globals.backendUri + '/category';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Create a new Category
   * @param category to create
   */
  createCategory(category: Category): Observable<string> {
    console.log(category);
    return this.httpClient.post(this.authBaseUri, category, {responseType: 'text'});
  }


  deleteCategory(id: number): Observable<Category> {
    console.log(id);
    return this.httpClient.delete<Category>(this.authBaseUri + '/' + id);
  }

  updateCategory(category: Category): Observable<Category> {
    console.log(category);
    return this.httpClient.patch<Category>(this.authBaseUri, category);

  }

  getCategoryById(category: Category): Observable<string> {
    console.log(category);
    return this.httpClient.get(this.authBaseUri + '/' + category.id, {responseType: 'text'});
  }

  getAllCategories(): Observable<Category[]> {
    return this.httpClient.get<Category[]>(this.authBaseUri);
  }

  getAllFoodCategories(): Observable<Category[]> {
    return this.httpClient.get<Category[]>(this.authBaseUri + '/food');
  }

  getAllDrinkCategories(): Observable<Category[]> {
    return this.httpClient.get<Category[]>(this.authBaseUri + '/drink');
  }
}
