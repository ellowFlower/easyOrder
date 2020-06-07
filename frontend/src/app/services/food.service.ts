import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Food} from '../dtos/food';
import {filter, map} from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class FoodService {

  private authBaseUri: string = this.globals.backendUri + '/foods';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   * @param authRequest User data
   */
  createFood(food: Food): Observable<Food> {
    return this.httpClient.post<Food>(this.authBaseUri, food);
  }

  listFood(): Observable<Food[]> {
    return this.httpClient.get<Food[]>(this.authBaseUri);
  }

  deleteFood(id: number): Observable<any> {
    return this.httpClient.delete(this.authBaseUri + '/' + id);
  }

  updateFood(food: Food): Observable<string> {
    return this.httpClient.patch(this.authBaseUri, food, {responseType: 'text'});
  }

  getFoodById(id: number): Observable<Food[]> {
    return this.httpClient.get<Food[]>(this.authBaseUri).pipe(
      map(results => results.filter(r => r.categoryIds.includes(id))));
  }

  findFoodsByName(name: string): Observable<Food[]> {
    return this.httpClient.get<Food[]>(this.authBaseUri + '/foodsByName', {
      params: {
        name: name
      }
    });
  }

  findFoodsByNameAndID(name: string, id: number): Observable<Food[]> {
    return this.httpClient.get<Food[]>(this.authBaseUri + '/foodsByName', {
      params: {
        name: name
      }
    }).pipe(
      map(results => results.filter(r => r.categoryIds.includes(id))));;
  }

}
