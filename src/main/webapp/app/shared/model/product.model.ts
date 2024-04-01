import { ICategory } from 'app/shared/model/category.model';

export interface IProduct {
  id?: number;
  name?: string | null;
  description?: string | null;
  price?: number | null;
  availableStockQuantity?: number | null;
  category?: ICategory | null;
}

export const defaultValue: Readonly<IProduct> = {};
