import dayjs from 'dayjs';
import { IAdministrator } from 'app/shared/model/administrator.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { IJorder } from 'app/shared/model/jorder.model';

export interface IHistoryEntry {
  id?: number;
  createdat?: string;
  updatedat?: string;
  type?: string;
  ispublic?: boolean;
  data?: string;
  discriminator?: string;
  administrator?: IAdministrator | null;
  customer?: ICustomer | null;
  jorder?: IJorder | null;
}

export const defaultValue: Readonly<IHistoryEntry> = {
  ispublic: false,
};
