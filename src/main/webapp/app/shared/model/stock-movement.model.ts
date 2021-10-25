import dayjs from 'dayjs';
import { IProductVariant } from 'app/shared/model/product-variant.model';
import { IOrderItem } from 'app/shared/model/order-item.model';
import { IOrderLine } from 'app/shared/model/order-line.model';

export interface IStockMovement {
  id?: number;
  createdat?: string;
  updatedat?: string;
  type?: string;
  quantity?: number;
  discriminator?: string;
  productvariant?: IProductVariant | null;
  orderitem?: IOrderItem | null;
  orderline?: IOrderLine | null;
}

export const defaultValue: Readonly<IStockMovement> = {};
