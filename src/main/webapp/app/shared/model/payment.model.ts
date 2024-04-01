import dayjs from 'dayjs';
import { IInvoice } from 'app/shared/model/invoice.model';
import { IOrder } from 'app/shared/model/order.model';

export interface IPayment {
  id?: number;
  status?: boolean | null;
  paymentDate?: dayjs.Dayjs | null;
  invoice?: IInvoice | null;
  order?: IOrder | null;
}

export const defaultValue: Readonly<IPayment> = {
  status: false,
};
