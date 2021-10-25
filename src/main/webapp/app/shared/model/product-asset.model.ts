import dayjs from 'dayjs';
import { IAsset } from 'app/shared/model/asset.model';
import { IProduct } from 'app/shared/model/product.model';

export interface IProductAsset {
  id?: number;
  createdat?: string;
  updatedat?: string;
  position?: number;
  asset?: IAsset;
  product?: IProduct;
}

export const defaultValue: Readonly<IProductAsset> = {};
