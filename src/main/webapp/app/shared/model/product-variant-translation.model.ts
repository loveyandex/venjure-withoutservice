import dayjs from 'dayjs';
import { IProductVariant } from 'app/shared/model/product-variant.model';

export interface IProductVariantTranslation {
  id?: number;
  createdat?: string;
  updatedat?: string;
  languagecode?: string;
  name?: string;
  base?: IProductVariant | null;
}

export const defaultValue: Readonly<IProductVariantTranslation> = {};
