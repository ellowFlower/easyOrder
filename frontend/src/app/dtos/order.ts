export class Order {
  constructor(
  public id: number,
  public status: string,
  public assistance: string,
  public startDate: Date,
  public endDate: Date,
  public foodsId: number[],
  public drinksId: number[],
  public tableId: number) {}
}
