import dayjs from 'dayjs';
import { IProduct } from 'app/shared/model/product.model';
import { IAsset } from 'app/shared/model/asset.model';
import { ITaxCategory } from 'app/shared/model/tax-category.model';
import { IChannel } from 'app/shared/model/channel.model';
import { ICollection } from 'app/shared/model/collection.model';
import { IFacetValue } from 'app/shared/model/facet-value.model';
import { IProductOption } from 'app/shared/model/product-option.model';
import { IProductVariantAsset } from 'app/shared/model/product-variant-asset.model';
import { IProductVariantPrice } from 'app/shared/model/product-variant-price.model';
import { IProductVariantTranslation } from 'app/shared/model/product-variant-translation.model';
import { IStockMovement } from 'app/shared/model/stock-movement.model';

export interface IProductVariant {
  id?: number;
  createdat?: string;
  updatedat?: string;
  deletedat?: string | null;
  enabled?: boolean;
  sku?: string;
  stockonhand?: number;
  stockallocated?: number;
  outofstockthreshold?: number;
  useglobaloutofstockthreshold?: boolean;
  trackinventory?: string;
  product?: IProduct | null;
  featuredasset?: IAsset | null;
  taxcategory?: ITaxCategory | null;
  channels?: IChannel[] | null;
  productVariants?: ICollection[] | null;
  facetValues?: IFacetValue[] | null;
  productOptions?: IProductOption[] | null;
  productVariantAssets?: IProductVariantAsset[] | null;
  productVariantPrices?: IProductVariantPrice[] | null;
  productVariantTranslations?: IProductVariantTranslation[] | null;
  stockMovements?: IStockMovement[] | null;
}

export const defaultValue: Readonly<IProductVariant> = {
  enabled: false,
  useglobaloutofstockthreshold: false,
};
