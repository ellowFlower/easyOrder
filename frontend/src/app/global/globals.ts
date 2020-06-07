import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class Globals {
  readonly backendUri: string = 'http://80.109.44.49:8080/api/v1';
}
