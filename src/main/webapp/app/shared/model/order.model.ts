import dayjs from 'dayjs';

export interface IOrder {
  id?: number;
  name?: string | null;
  orderDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IOrder> = {};
