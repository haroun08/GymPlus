import { IPeriod } from 'app/shared/model/period.model';

export interface IPlan {
  id?: number;
  name?: string | null;
  price?: number | null;
  periods?: IPeriod | null;
}

export const defaultValue: Readonly<IPlan> = {};
