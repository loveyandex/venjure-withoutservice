import dayjs from 'dayjs';
import { IProductVariant } from 'app/shared/model/product-variant.model';

export interface IProductVariantPrice {
  id?: number;
  createdat?: string;
  updatedat?: string;
  price?: number;
  channelid?: number;
  variant?: IProductVariant | null;
}

export const defaultValue: Readonly<IProductVariantPrice> = {};
