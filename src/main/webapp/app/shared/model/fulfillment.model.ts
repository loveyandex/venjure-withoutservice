import dayjs from 'dayjs';
import { IOrderItem } from 'app/shared/model/order-item.model';

export interface IFulfillment {
  id?: number;
  createdat?: string;
  updatedat?: string;
  state?: string;
  trackingcode?: string;
  method?: string;
  handlercode?: string;
  orderItems?: IOrderItem[] | null;
}

export const defaultValue: Readonly<IFulfillment> = {};
