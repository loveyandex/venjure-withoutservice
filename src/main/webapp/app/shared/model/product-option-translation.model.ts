import dayjs from 'dayjs';
import { IProductOption } from 'app/shared/model/product-option.model';

export interface IProductOptionTranslation {
  id?: number;
  createdat?: string;
  updatedat?: string;
  languagecode?: string;
  name?: string;
  base?: IProductOption | null;
}

export const defaultValue: Readonly<IProductOptionTranslation> = {};
