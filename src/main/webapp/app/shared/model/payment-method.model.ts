import dayjs from 'dayjs';
import { IChannel } from 'app/shared/model/channel.model';

export interface IPaymentMethod {
  id?: number;
  createdat?: string;
  updatedat?: string;
  name?: string;
  code?: string;
  description?: string;
  enabled?: boolean;
  checker?: string | null;
  handler?: string;
  channels?: IChannel[] | null;
}

export const defaultValue: Readonly<IPaymentMethod> = {
  enabled: false,
};
