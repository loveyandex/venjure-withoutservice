import dayjs from 'dayjs';
import { IJorder } from 'app/shared/model/jorder.model';
import { IChannel } from 'app/shared/model/channel.model';

export interface IPromotion {
  id?: number;
  createdat?: string;
  updatedat?: string;
  deletedat?: string | null;
  startsat?: string | null;
  endsat?: string | null;
  couponcode?: string | null;
  percustomerusagelimit?: number | null;
  name?: string;
  enabled?: boolean;
  conditions?: string;
  actions?: string;
  priorityscore?: number;
  jorders?: IJorder[] | null;
  channels?: IChannel[] | null;
}

export const defaultValue: Readonly<IPromotion> = {
  enabled: false,
};
