import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Drink} from '../dtos/drink';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class DrinkService {

  private authBaseUri: string = this.globals.backendUri + '/drinks';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  createDrink(drink: Drink): Observable<Drink> {
    return this.httpClient.post<Drink>(this.authBaseUri, drink);
  }

  listDrink(): Observable<Drink[]> {
    return this.httpClient.get<Drink[]>(this.authBaseUri);
  }

  deleteDrink(id: number): Observable<any> {
    return this.httpClient.delete(this.authBaseUri + '/' + id);
  }

  updateDrink(drink: Drink): Observable<string> {
    return this.httpClient.patch(this.authBaseUri, drink, {responseType: 'text'});
  }

  getDrinkById(id: number): Observable<Drink[]> {
    return this.httpClient.get<Drink[]>(this.authBaseUri).pipe(
      map(results => results.filter(r => r.categoryIds.includes(id))));
  }

  findDrinksByName(name: string): Observable<Drink[]> {
    return this.httpClient.get<Drink[]>(this.authBaseUri + '/drinksByName', {
      params: {
        name: name
      }
    });
  }

  findDrinksByNameAndID(name: string, id: number) {
    return this.httpClient.get<Drink[]>(this.authBaseUri + '/drinksByName', {
      params: {
        name: name
      }
    }).pipe(
      map(results => results.filter(r => r.categoryIds.includes(id))));
  }
}
