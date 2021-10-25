import dayjs from 'dayjs';
import { IAsset } from 'app/shared/model/asset.model';
import { ICollectionTranslation } from 'app/shared/model/collection-translation.model';
import { IProductVariant } from 'app/shared/model/product-variant.model';

export interface ICollection {
  id?: number;
  createdat?: string;
  updatedat?: string;
  isroot?: boolean;
  position?: number;
  isprivate?: boolean;
  filters?: string;
  featuredasset?: IAsset | null;
  parent?: ICollection | null;
  collectionTranslations?: ICollectionTranslation[] | null;
  productvariants?: IProductVariant[] | null;
}

export const defaultValue: Readonly<ICollection> = {
  isroot: false,
  isprivate: false,
};
