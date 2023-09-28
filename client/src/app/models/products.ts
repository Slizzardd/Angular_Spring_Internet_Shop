export interface IProducts {
  id: number,
  title: string,
  price: number,
  year: number,
  category: string,
  image?: string,
  description: any,
  quantityProductOnBasket : number,

  quantityInWarehouse: number;
}
