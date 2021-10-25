import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IHistoryEntry } from 'app/shared/model/history-entry.model';

export interface IAdministrator {
  id?: number;
  createdat?: string;
  updatedat?: string;
  deletedat?: string | null;
  firstname?: string;
  lastname?: string;
  emailaddress?: string;
  user?: IUser | null;
  historyEntries?: IHistoryEntry[] | null;
}

export const defaultValue: Readonly<IAdministrator> = {};
