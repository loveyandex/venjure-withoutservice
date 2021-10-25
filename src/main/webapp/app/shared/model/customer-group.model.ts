import dayjs from 'dayjs';
import { ICustomer } from 'app/shared/model/customer.model';

export interface ICustomerGroup {
  id?: number;
  createdat?: string;
  updatedat?: string;
  name?: string;
  customers?: ICustomer[] | null;
}

export const defaultValue: Readonly<ICustomerGroup> = {};
