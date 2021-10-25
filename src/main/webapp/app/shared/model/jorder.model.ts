import dayjs from 'dayjs';
import { ICustomer } from 'app/shared/model/customer.model';
import { IChannel } from 'app/shared/model/channel.model';
import { IPromotion } from 'app/shared/model/promotion.model';
import { IHistoryEntry } from 'app/shared/model/history-entry.model';
import { IOrderLine } from 'app/shared/model/order-line.model';
import { IOrderModification } from 'app/shared/model/order-modification.model';
import { IPayment } from 'app/shared/model/payment.model';
import { IShippingLine } from 'app/shared/model/shipping-line.model';
import { ISurcharge } from 'app/shared/model/surcharge.model';

export interface IJorder {
  id?: number;
  createdat?: string;
  updatedat?: string;
  code?: string;
  state?: string;
  active?: boolean;
  orderplacedat?: string | null;
  couponcodes?: string;
  shippingaddress?: string;
  billingaddress?: string;
  currencycode?: string;
  subtotal?: number;
  subtotalwithtax?: number;
  shipping?: number;
  shippingwithtax?: number;
  taxzoneid?: number | null;
  customer?: ICustomer | null;
  channels?: IChannel[] | null;
  promotions?: IPromotion[] | null;
  historyEntries?: IHistoryEntry[] | null;
  orderLines?: IOrderLine[] | null;
  orderModifications?: IOrderModification[] | null;
  payments?: IPayment[] | null;
  shippingLines?: IShippingLine[] | null;
  surcharges?: ISurcharge[] | null;
}

export const defaultValue: Readonly<IJorder> = {
  active: false,
};
