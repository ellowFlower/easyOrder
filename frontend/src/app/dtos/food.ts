import {Category} from './category';

export class Food {
  constructor(
    public id: number,
    public name: string,
    public description: string,
    public price: number,
    public image: any,
    public imageContentType: any,
    public categoryIds: number[],
    public deleted: Boolean,
    public updated: Boolean
  ) {
  }
}
