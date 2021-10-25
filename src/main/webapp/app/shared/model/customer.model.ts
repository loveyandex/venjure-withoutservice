import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IAsset } from 'app/shared/model/asset.model';
import { IChannel } from 'app/shared/model/channel.model';
import { ICustomerGroup } from 'app/shared/model/customer-group.model';
import { IAddress } from 'app/shared/model/address.model';
import { IHistoryEntry } from 'app/shared/model/history-entry.model';
import { IJorder } from 'app/shared/model/jorder.model';

export interface ICustomer {
  id?: number;
  createdat?: string;
  updatedat?: string;
  deletedat?: string | null;
  title?: string | null;
  firstname?: string;
  lastname?: string;
  phonenumber?: string | null;
  emailaddress?: string;
  user?: IUser | null;
  avatar?: IAsset | null;
  channels?: IChannel[] | null;
  customerGroups?: ICustomerGroup[] | null;
  addresses?: IAddress[] | null;
  historyEntries?: IHistoryEntry[] | null;
  jorders?: IJorder[] | null;
}

export const defaultValue: Readonly<ICustomer> = {};
