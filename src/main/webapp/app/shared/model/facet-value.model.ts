import dayjs from 'dayjs';
import { IFacet } from 'app/shared/model/facet.model';
import { IChannel } from 'app/shared/model/channel.model';
import { IProduct } from 'app/shared/model/product.model';
import { IFacetValueTranslation } from 'app/shared/model/facet-value-translation.model';
import { IProductVariant } from 'app/shared/model/product-variant.model';

export interface IFacetValue {
  id?: number;
  createdat?: string;
  updatedat?: string;
  code?: string;
  facet?: IFacet | null;
  channels?: IChannel[] | null;
  products?: IProduct[] | null;
  facetValueTranslations?: IFacetValueTranslation[] | null;
  productVariants?: IProductVariant[] | null;
}

export const defaultValue: Readonly<IFacetValue> = {};
