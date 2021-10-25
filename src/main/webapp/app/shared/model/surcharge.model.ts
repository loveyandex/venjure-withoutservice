import dayjs from 'dayjs';
import { IJorder } from 'app/shared/model/jorder.model';
import { IOrderModification } from 'app/shared/model/order-modification.model';

export interface ISurcharge {
  id?: number;
  createdat?: string;
  updatedat?: string;
  description?: string;
  listprice?: number;
  listpriceincludestax?: boolean;
  sku?: string;
  taxlines?: string;
  jorder?: IJorder | null;
  ordermodification?: IOrderModification | null;
}

export const defaultValue: Readonly<ISurcharge> = {
  listpriceincludestax: false,
};
