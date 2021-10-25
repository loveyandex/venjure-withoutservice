import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/payment.model';
import { IRefund } from 'app/shared/model/refund.model';
import { IJorder } from 'app/shared/model/jorder.model';
import { ISurcharge } from 'app/shared/model/surcharge.model';
import { IOrderItem } from 'app/shared/model/order-item.model';

export interface IOrderModification {
  id?: number;
  createdat?: string;
  updatedat?: string;
  note?: string;
  pricechange?: number;
  shippingaddresschange?: string | null;
  billingaddresschange?: string | null;
  payment?: IPayment | null;
  refund?: IRefund | null;
  jorder?: IJorder | null;
  surcharges?: ISurcharge[] | null;
  orderItems?: IOrderItem[] | null;
}

export const defaultValue: Readonly<IOrderModification> = {};
