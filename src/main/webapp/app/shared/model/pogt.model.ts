import dayjs from 'dayjs';
import { IProductOptionGroup } from 'app/shared/model/product-option-group.model';

export interface IPogt {
  id?: number;
  createdat?: string;
  updatedat?: string;
  languagecode?: string;
  name?: string;
  base?: IProductOptionGroup | null;
}

export const defaultValue: Readonly<IPogt> = {};
