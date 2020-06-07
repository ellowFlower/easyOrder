import {SimpleFood} from './simpleFood';
import {SimpleDrink} from './simpleDrink';

export class OrderFindAll {
  constructor(
    public id: number,
    public status: string,
    public tableId: string,
    public assistance: string,
    public startDate: string,
    public endDate: string,
    public foods: SimpleFood[],
    public drinks: SimpleDrink[]) {}
}
