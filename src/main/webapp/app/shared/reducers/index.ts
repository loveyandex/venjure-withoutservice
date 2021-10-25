import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import address, {
  AddressState
} from 'app/entities/address/address.reducer';
// prettier-ignore
import administrator, {
  AdministratorState
} from 'app/entities/administrator/administrator.reducer';
// prettier-ignore
import asset, {
  AssetState
} from 'app/entities/asset/asset.reducer';
// prettier-ignore
import channel, {
  ChannelState
} from 'app/entities/channel/channel.reducer';
// prettier-ignore
import collection, {
  CollectionState
} from 'app/entities/collection/collection.reducer';
// prettier-ignore
import collectionAsset, {
  CollectionAssetState
} from 'app/entities/collection-asset/collection-asset.reducer';
// prettier-ignore
import collectionTranslation, {
  CollectionTranslationState
} from 'app/entities/collection-translation/collection-translation.reducer';
// prettier-ignore
import country, {
  CountryState
} from 'app/entities/country/country.reducer';
// prettier-ignore
import countryTranslation, {
  CountryTranslationState
} from 'app/entities/country-translation/country-translation.reducer';
// prettier-ignore
import customer, {
  CustomerState
} from 'app/entities/customer/customer.reducer';
// prettier-ignore
import customerGroup, {
  CustomerGroupState
} from 'app/entities/customer-group/customer-group.reducer';
// prettier-ignore
import exampleEntity, {
  ExampleEntityState
} from 'app/entities/example-entity/example-entity.reducer';
// prettier-ignore
import facet, {
  FacetState
} from 'app/entities/facet/facet.reducer';
// prettier-ignore
import facetTranslation, {
  FacetTranslationState
} from 'app/entities/facet-translation/facet-translation.reducer';
// prettier-ignore
import facetValue, {
  FacetValueState
} from 'app/entities/facet-value/facet-value.reducer';
// prettier-ignore
import facetValueTranslation, {
  FacetValueTranslationState
} from 'app/entities/facet-value-translation/facet-value-translation.reducer';
// prettier-ignore
import fulfillment, {
  FulfillmentState
} from 'app/entities/fulfillment/fulfillment.reducer';
// prettier-ignore
import globalSettings, {
  GlobalSettingsState
} from 'app/entities/global-settings/global-settings.reducer';
// prettier-ignore
import historyEntry, {
  HistoryEntryState
} from 'app/entities/history-entry/history-entry.reducer';
// prettier-ignore
import jobRecord, {
  JobRecordState
} from 'app/entities/job-record/job-record.reducer';
// prettier-ignore
import jorder, {
  JorderState
} from 'app/entities/jorder/jorder.reducer';
// prettier-ignore
import orderItem, {
  OrderItemState
} from 'app/entities/order-item/order-item.reducer';
// prettier-ignore
import orderLine, {
  OrderLineState
} from 'app/entities/order-line/order-line.reducer';
// prettier-ignore
import orderModification, {
  OrderModificationState
} from 'app/entities/order-modification/order-modification.reducer';
// prettier-ignore
import payment, {
  PaymentState
} from 'app/entities/payment/payment.reducer';
// prettier-ignore
import paymentMethod, {
  PaymentMethodState
} from 'app/entities/payment-method/payment-method.reducer';
// prettier-ignore
import product, {
  ProductState
} from 'app/entities/product/product.reducer';
// prettier-ignore
import productAsset, {
  ProductAssetState
} from 'app/entities/product-asset/product-asset.reducer';
// prettier-ignore
import productOption, {
  ProductOptionState
} from 'app/entities/product-option/product-option.reducer';
// prettier-ignore
import productOptionGroup, {
  ProductOptionGroupState
} from 'app/entities/product-option-group/product-option-group.reducer';
// prettier-ignore
import pogt, {
  PogtState
} from 'app/entities/pogt/pogt.reducer';
// prettier-ignore
import productOptionTranslation, {
  ProductOptionTranslationState
} from 'app/entities/product-option-translation/product-option-translation.reducer';
// prettier-ignore
import productTranslation, {
  ProductTranslationState
} from 'app/entities/product-translation/product-translation.reducer';
// prettier-ignore
import productVariant, {
  ProductVariantState
} from 'app/entities/product-variant/product-variant.reducer';
// prettier-ignore
import productVariantAsset, {
  ProductVariantAssetState
} from 'app/entities/product-variant-asset/product-variant-asset.reducer';
// prettier-ignore
import productVariantPrice, {
  ProductVariantPriceState
} from 'app/entities/product-variant-price/product-variant-price.reducer';
// prettier-ignore
import productVariantTranslation, {
  ProductVariantTranslationState
} from 'app/entities/product-variant-translation/product-variant-translation.reducer';
// prettier-ignore
import promotion, {
  PromotionState
} from 'app/entities/promotion/promotion.reducer';
// prettier-ignore
import refund, {
  RefundState
} from 'app/entities/refund/refund.reducer';
// prettier-ignore
import shippingLine, {
  ShippingLineState
} from 'app/entities/shipping-line/shipping-line.reducer';
// prettier-ignore
import shippingMethod, {
  ShippingMethodState
} from 'app/entities/shipping-method/shipping-method.reducer';
// prettier-ignore
import shippingMethodTranslation, {
  ShippingMethodTranslationState
} from 'app/entities/shipping-method-translation/shipping-method-translation.reducer';
// prettier-ignore
import stockMovement, {
  StockMovementState
} from 'app/entities/stock-movement/stock-movement.reducer';
// prettier-ignore
import surcharge, {
  SurchargeState
} from 'app/entities/surcharge/surcharge.reducer';
// prettier-ignore
import tag, {
  TagState
} from 'app/entities/tag/tag.reducer';
// prettier-ignore
import taxCategory, {
  TaxCategoryState
} from 'app/entities/tax-category/tax-category.reducer';
// prettier-ignore
import taxRate, {
  TaxRateState
} from 'app/entities/tax-rate/tax-rate.reducer';
// prettier-ignore
import zone, {
  ZoneState
} from 'app/entities/zone/zone.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly address: AddressState;
  readonly administrator: AdministratorState;
  readonly asset: AssetState;
  readonly channel: ChannelState;
  readonly collection: CollectionState;
  readonly collectionAsset: CollectionAssetState;
  readonly collectionTranslation: CollectionTranslationState;
  readonly country: CountryState;
  readonly countryTranslation: CountryTranslationState;
  readonly customer: CustomerState;
  readonly customerGroup: CustomerGroupState;
  readonly exampleEntity: ExampleEntityState;
  readonly facet: FacetState;
  readonly facetTranslation: FacetTranslationState;
  readonly facetValue: FacetValueState;
  readonly facetValueTranslation: FacetValueTranslationState;
  readonly fulfillment: FulfillmentState;
  readonly globalSettings: GlobalSettingsState;
  readonly historyEntry: HistoryEntryState;
  readonly jobRecord: JobRecordState;
  readonly jorder: JorderState;
  readonly orderItem: OrderItemState;
  readonly orderLine: OrderLineState;
  readonly orderModification: OrderModificationState;
  readonly payment: PaymentState;
  readonly paymentMethod: PaymentMethodState;
  readonly product: ProductState;
  readonly productAsset: ProductAssetState;
  readonly productOption: ProductOptionState;
  readonly productOptionGroup: ProductOptionGroupState;
  readonly pogt: PogtState;
  readonly productOptionTranslation: ProductOptionTranslationState;
  readonly productTranslation: ProductTranslationState;
  readonly productVariant: ProductVariantState;
  readonly productVariantAsset: ProductVariantAssetState;
  readonly productVariantPrice: ProductVariantPriceState;
  readonly productVariantTranslation: ProductVariantTranslationState;
  readonly promotion: PromotionState;
  readonly refund: RefundState;
  readonly shippingLine: ShippingLineState;
  readonly shippingMethod: ShippingMethodState;
  readonly shippingMethodTranslation: ShippingMethodTranslationState;
  readonly stockMovement: StockMovementState;
  readonly surcharge: SurchargeState;
  readonly tag: TagState;
  readonly taxCategory: TaxCategoryState;
  readonly taxRate: TaxRateState;
  readonly zone: ZoneState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  address,
  administrator,
  asset,
  channel,
  collection,
  collectionAsset,
  collectionTranslation,
  country,
  countryTranslation,
  customer,
  customerGroup,
  exampleEntity,
  facet,
  facetTranslation,
  facetValue,
  facetValueTranslation,
  fulfillment,
  globalSettings,
  historyEntry,
  jobRecord,
  jorder,
  orderItem,
  orderLine,
  orderModification,
  payment,
  paymentMethod,
  product,
  productAsset,
  productOption,
  productOptionGroup,
  pogt,
  productOptionTranslation,
  productTranslation,
  productVariant,
  productVariantAsset,
  productVariantPrice,
  productVariantTranslation,
  promotion,
  refund,
  shippingLine,
  shippingMethod,
  shippingMethodTranslation,
  stockMovement,
  surcharge,
  tag,
  taxCategory,
  taxRate,
  zone,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;
