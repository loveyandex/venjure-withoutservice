import dayjs from 'dayjs';
import { IShippingMethodTranslation } from 'app/shared/model/shipping-method-translation.model';
import { IChannel } from 'app/shared/model/channel.model';

export interface IShippingMethod {
  id?: number;
  createdat?: string;
  updatedat?: string;
  deletedat?: string | null;
  code?: string;
  checker?: string;
  calculator?: string;
  fulfillmenthandlercode?: string;
  shippingMethodTranslations?: IShippingMethodTranslation[] | null;
  channels?: IChannel[] | null;
}

export const defaultValue: Readonly<IShippingMethod> = {};
