export class Drink {
  constructor(
    public id: number,
    public name: string,
    public description: string,
    public price: number,
    public alcohol: number,
    public image: any,
    public imageContentType: any,
    public categoryIds: number[],
    public deleted: Boolean,
    public updated: Boolean
) {
  }
}
