import dayjs from 'dayjs';
import { IJorder } from 'app/shared/model/jorder.model';
import { IRefund } from 'app/shared/model/refund.model';

export interface IPayment {
  id?: number;
  createdat?: string;
  updatedat?: string;
  method?: string;
  amount?: number;
  state?: string;
  errormessage?: string | null;
  transactionid?: string | null;
  metadata?: string;
  jorder?: IJorder | null;
  refunds?: IRefund[] | null;
}

export const defaultValue: Readonly<IPayment> = {};
