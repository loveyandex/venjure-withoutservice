import dayjs from 'dayjs';
import { IProductOptionGroup } from 'app/shared/model/product-option-group.model';
import { IProductOptionTranslation } from 'app/shared/model/product-option-translation.model';
import { IProductVariant } from 'app/shared/model/product-variant.model';

export interface IProductOption {
  id?: number;
  createdat?: string;
  updatedat?: string;
  deletedat?: string | null;
  code?: string;
  group?: IProductOptionGroup;
  productOptionTranslations?: IProductOptionTranslation[] | null;
  productVariants?: IProductVariant[] | null;
}

export const defaultValue: Readonly<IProductOption> = {};
