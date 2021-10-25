import dayjs from 'dayjs';
import { IZone } from 'app/shared/model/zone.model';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { IProduct } from 'app/shared/model/product.model';
import { IPromotion } from 'app/shared/model/promotion.model';
import { IShippingMethod } from 'app/shared/model/shipping-method.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { IFacet } from 'app/shared/model/facet.model';
import { IFacetValue } from 'app/shared/model/facet-value.model';
import { IJorder } from 'app/shared/model/jorder.model';
import { IProductVariant } from 'app/shared/model/product-variant.model';

export interface IChannel {
  id?: number;
  createdat?: string;
  updatedat?: string;
  code?: string;
  token?: string;
  defaultlanguagecode?: string;
  currencycode?: string;
  pricesincludetax?: boolean;
  defaulttaxzone?: IZone | null;
  defaultshippingzone?: IZone | null;
  paymentMethods?: IPaymentMethod[] | null;
  products?: IProduct[] | null;
  promotions?: IPromotion[] | null;
  shippingMethods?: IShippingMethod[] | null;
  customers?: ICustomer[] | null;
  facets?: IFacet[] | null;
  facetValues?: IFacetValue[] | null;
  jorders?: IJorder[] | null;
  productVariants?: IProductVariant[] | null;
}

export const defaultValue: Readonly<IChannel> = {
  pricesincludetax: false,
};
