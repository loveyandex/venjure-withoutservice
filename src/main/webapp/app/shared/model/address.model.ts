import dayjs from 'dayjs';
import { ICustomer } from 'app/shared/model/customer.model';
import { ICountry } from 'app/shared/model/country.model';

export interface IAddress {
  id?: number;
  createdat?: string;
  updatedat?: string;
  fullname?: string;
  company?: string;
  streetline1?: string;
  streetline2?: string;
  city?: string;
  province?: string;
  postalcode?: string;
  phonenumber?: string;
  defaultshippingaddress?: boolean;
  defaultbillingaddress?: boolean;
  customer?: ICustomer | null;
  country?: ICountry | null;
}

export const defaultValue: Readonly<IAddress> = {
  defaultshippingaddress: false,
  defaultbillingaddress: false,
};
