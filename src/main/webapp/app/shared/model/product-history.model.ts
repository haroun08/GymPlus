import dayjs from 'dayjs';
import { IProduct } from 'app/shared/model/product.model';

export interface IProductHistory {
  id?: number;
  name?: string | null;
  description?: string | null;
  price?: number | null;
  availableStockQuantity?: number | null;
  validFrom?: dayjs.Dayjs | null;
  validTo?: dayjs.Dayjs | null;
  product?: IProduct | null;
}

export const defaultValue: Readonly<IProductHistory> = {};
