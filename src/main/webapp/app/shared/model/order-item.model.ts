import dayjs from 'dayjs';
import { IOrderLine } from 'app/shared/model/order-line.model';
import { IRefund } from 'app/shared/model/refund.model';
import { IFulfillment } from 'app/shared/model/fulfillment.model';
import { IOrderModification } from 'app/shared/model/order-modification.model';
import { IStockMovement } from 'app/shared/model/stock-movement.model';

export interface IOrderItem {
  id?: number;
  createdat?: string;
  updatedat?: string;
  initiallistprice?: number | null;
  listprice?: number;
  listpriceincludestax?: boolean;
  adjustments?: string;
  taxlines?: string;
  cancelled?: boolean;
  line?: IOrderLine;
  refund?: IRefund | null;
  fulfillments?: IFulfillment[] | null;
  orderModifications?: IOrderModification[] | null;
  stockMovements?: IStockMovement[] | null;
}

export const defaultValue: Readonly<IOrderItem> = {
  listpriceincludestax: false,
  cancelled: false,
};
