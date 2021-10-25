import dayjs from 'dayjs';
import { IAsset } from 'app/shared/model/asset.model';
import { IProductVariant } from 'app/shared/model/product-variant.model';

export interface IProductVariantAsset {
  id?: number;
  createdat?: string;
  updatedat?: string;
  position?: number;
  asset?: IAsset;
  productvariant?: IProductVariant;
}

export const defaultValue: Readonly<IProductVariantAsset> = {};
