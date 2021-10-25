import dayjs from 'dayjs';
import { IShippingMethod } from 'app/shared/model/shipping-method.model';
import { IJorder } from 'app/shared/model/jorder.model';

export interface IShippingLine {
  id?: number;
  createdat?: string;
  updatedat?: string;
  listprice?: number;
  listpriceincludestax?: boolean;
  adjustments?: string;
  taxlines?: string;
  shippingmethod?: IShippingMethod;
  jorder?: IJorder | null;
}

export const defaultValue: Readonly<IShippingLine> = {
  listpriceincludestax: false,
};
