import { IUser } from 'app/shared/model/user.model';
import { IPlan } from 'app/shared/model/plan.model';
import { IInvoice } from 'app/shared/model/invoice.model';

export interface IPeriodicSubscription {
  id?: number;
  name?: string | null;
  description?: string | null;
  status?: boolean | null;
  paymentStatus?: boolean | null;
  ids?: IUser[] | null;
  plans?: IPlan | null;
  invoices?: IInvoice | null;
}

export const defaultValue: Readonly<IPeriodicSubscription> = {
  status: false,
  paymentStatus: false,
};
