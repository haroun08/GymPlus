import { IUser } from 'app/shared/model/user.model';

export interface IGym {
  id?: number;
  name?: string | null;
  streetAddress?: string | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  phoneNumber?: number | null;
  ids?: IUser[] | null;
}

export const defaultValue: Readonly<IGym> = {};
