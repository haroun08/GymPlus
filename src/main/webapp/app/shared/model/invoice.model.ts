import dayjs from 'dayjs';

export interface IInvoice {
  id?: number;
  invoiceDate?: dayjs.Dayjs | null;
  payAmount?: number | null;
}

export const defaultValue: Readonly<IInvoice> = {};
