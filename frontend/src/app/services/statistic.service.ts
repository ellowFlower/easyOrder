import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Statistic} from '../dtos/statistic';

@Injectable({
  providedIn: 'root'
})
export class StatisticService {

  private authBaseUri: string = this.globals.backendUri + '/statistic';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  overallTurnover(): Observable<Statistic> {
    return this.httpClient.get<Statistic>(this.authBaseUri + '/turnover/overall');
  }

  overallSales(): Observable<Statistic> {
    return this.httpClient.get<Statistic>(this.authBaseUri + '/sales/overall');
  }

  calculateTimeRangeTurnoverStatistic(from: Date, to: Date): Observable<Statistic> {
    const params = new HttpParams()
      .set('from', ('' + from.getFullYear() + '-' + from.getMonth() + '-' + from.getDate()))
      .set('to', ('' + to.getFullYear() + '-' + to.getMonth() + '-' + to.getDate()));
    return this.httpClient.get<Statistic>(this.authBaseUri + '/turnover', {params});
  }

  calculateTimeRangeSalesStatistic(from: Date, to: Date): Observable<Statistic> {
    const params = new HttpParams()
      .set('from', ('' + from.getFullYear() + '-' + from.getMonth() + '-' + from.getDate()))
      .set('to', ('' + to.getFullYear() + '-' + to.getMonth() + '-' + to.getDate()));
    return this.httpClient.get<Statistic>(this.authBaseUri + '/sales', {params});
  }
}
