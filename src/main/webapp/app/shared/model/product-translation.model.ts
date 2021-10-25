import dayjs from 'dayjs';
import { IProduct } from 'app/shared/model/product.model';

export interface IProductTranslation {
  id?: number;
  createdat?: string;
  updatedat?: string;
  languagecode?: string;
  name?: string;
  slug?: string;
  description?: string;
  base?: IProduct | null;
}

export const defaultValue: Readonly<IProductTranslation> = {};
