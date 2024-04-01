import { IOrder } from 'app/shared/model/order.model';
import { IProduct } from 'app/shared/model/product.model';

export interface IOrderUnit {
  id?: number;
  quantity?: number | null;
  unitPrice?: number | null;
  orderUnits?: IOrder | null;
  products?: IProduct | null;
}

export const defaultValue: Readonly<IOrderUnit> = {};
