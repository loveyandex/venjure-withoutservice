import dayjs from 'dayjs';
import { IAsset } from 'app/shared/model/asset.model';
import { IProductVariant } from 'app/shared/model/product-variant.model';
import { IChannel } from 'app/shared/model/channel.model';
import { IFacetValue } from 'app/shared/model/facet-value.model';

export interface IProduct {
  id?: number;
  createdat?: string;
  updatedat?: string;
  deletedat?: string | null;
  enabled?: boolean;
  featuredasset?: IAsset | null;
  productVariants?: IProductVariant[] | null;
  channels?: IChannel[] | null;
  facetValues?: IFacetValue[] | null;
}

export const defaultValue: Readonly<IProduct> = {
  enabled: false,
};
