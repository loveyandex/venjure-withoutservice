import dayjs from 'dayjs';
import { IProduct } from 'app/shared/model/product.model';
import { IProductOption } from 'app/shared/model/product-option.model';
import { IPogt } from 'app/shared/model/pogt.model';

export interface IProductOptionGroup {
  id?: number;
  createdat?: string;
  updatedat?: string;
  deletedat?: string | null;
  code?: string;
  product?: IProduct | null;
  productOptions?: IProductOption[] | null;
  productOptionGroupTranslations?: IPogt[] | null;
}

export const defaultValue: Readonly<IProductOptionGroup> = {};
