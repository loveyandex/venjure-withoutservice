import dayjs from 'dayjs';
import { IProductVariant } from 'app/shared/model/product-variant.model';
import { ITaxCategory } from 'app/shared/model/tax-category.model';
import { IAsset } from 'app/shared/model/asset.model';
import { IJorder } from 'app/shared/model/jorder.model';
import { IOrderItem } from 'app/shared/model/order-item.model';
import { IStockMovement } from 'app/shared/model/stock-movement.model';

export interface IOrderLine {
  id?: number;
  createdat?: string;
  updatedat?: string;
  productvariant?: IProductVariant | null;
  taxcategory?: ITaxCategory | null;
  featuredAsset?: IAsset | null;
  jorder?: IJorder | null;
  orderItems?: IOrderItem[] | null;
  stockMovements?: IStockMovement[] | null;
}

export const defaultValue: Readonly<IOrderLine> = {};
