import dayjs from 'dayjs';
import { ITaxCategory } from 'app/shared/model/tax-category.model';
import { IZone } from 'app/shared/model/zone.model';
import { ICustomerGroup } from 'app/shared/model/customer-group.model';

export interface ITaxRate {
  id?: number;
  createdat?: string;
  updatedat?: string;
  name?: string;
  enabled?: boolean;
  value?: number;
  category?: ITaxCategory | null;
  zone?: IZone | null;
  customergroup?: ICustomerGroup | null;
}

export const defaultValue: Readonly<ITaxRate> = {
  enabled: false,
};
