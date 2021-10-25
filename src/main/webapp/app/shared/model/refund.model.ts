import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/payment.model';
import { IOrderItem } from 'app/shared/model/order-item.model';

export interface IRefund {
  id?: number;
  createdat?: string;
  updatedat?: string;
  items?: number;
  shipping?: number;
  adjustment?: number;
  total?: number;
  method?: string;
  reason?: string | null;
  state?: string;
  transactionid?: string | null;
  metadata?: string;
  payment?: IPayment;
  orderItems?: IOrderItem[] | null;
}

export const defaultValue: Readonly<IRefund> = {};
