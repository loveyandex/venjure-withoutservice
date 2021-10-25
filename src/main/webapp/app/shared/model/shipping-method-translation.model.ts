import dayjs from 'dayjs';
import { IShippingMethod } from 'app/shared/model/shipping-method.model';

export interface IShippingMethodTranslation {
  id?: number;
  createdat?: string;
  updatedat?: string;
  languagecode?: string;
  name?: string;
  description?: string;
  base?: IShippingMethod | null;
}

export const defaultValue: Readonly<IShippingMethodTranslation> = {};
