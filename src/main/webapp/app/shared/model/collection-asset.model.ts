import dayjs from 'dayjs';
import { IAsset } from 'app/shared/model/asset.model';
import { ICollection } from 'app/shared/model/collection.model';

export interface ICollectionAsset {
  id?: number;
  createdat?: string;
  updatedat?: string;
  position?: number;
  asset?: IAsset;
  collection?: ICollection;
}

export const defaultValue: Readonly<ICollectionAsset> = {};
