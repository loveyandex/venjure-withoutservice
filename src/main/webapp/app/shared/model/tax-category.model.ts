import dayjs from 'dayjs';
import { ITaxRate } from 'app/shared/model/tax-rate.model';

export interface ITaxCategory {
  id?: number;
  createdat?: string;
  updatedat?: string;
  name?: string;
  isdefault?: boolean;
  taxRates?: ITaxRate[] | null;
}

export const defaultValue: Readonly<ITaxCategory> = {
  isdefault: false,
};
