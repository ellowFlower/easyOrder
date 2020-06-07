import {ItemStat} from './itemStat';

export class Statistic {
  constructor(
    public start: Date,
    public end: Date,
    public foodStats: ItemStat[],
    public drinkStats: ItemStat[]
  ) {
  }
}
