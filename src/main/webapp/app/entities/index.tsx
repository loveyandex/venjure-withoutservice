import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Address from './address';
import Administrator from './administrator';
import Asset from './asset';
import Channel from './channel';
import Collection from './collection';
import CollectionAsset from './collection-asset';
import CollectionTranslation from './collection-translation';
import Country from './country';
import CountryTranslation from './country-translation';
import Customer from './customer';
import CustomerGroup from './customer-group';
import ExampleEntity from './example-entity';
import Facet from './facet';
import FacetTranslation from './facet-translation';
import FacetValue from './facet-value';
import FacetValueTranslation from './facet-value-translation';
import Fulfillment from './fulfillment';
import GlobalSettings from './global-settings';
import HistoryEntry from './history-entry';
import JobRecord from './job-record';
import Jorder from './jorder';
import OrderItem from './order-item';
import OrderLine from './order-line';
import OrderModification from './order-modification';
import Payment from './payment';
import PaymentMethod from './payment-method';
import Product from './product';
import ProductAsset from './product-asset';
import ProductOption from './product-option';
import ProductOptionGroup from './product-option-group';
import Pogt from './pogt';
import ProductOptionTranslation from './product-option-translation';
import ProductTranslation from './product-translation';
import ProductVariant from './product-variant';
import ProductVariantAsset from './product-variant-asset';
import ProductVariantPrice from './product-variant-price';
import ProductVariantTranslation from './product-variant-translation';
import Promotion from './promotion';
import Refund from './refund';
import ShippingLine from './shipping-line';
import ShippingMethod from './shipping-method';
import ShippingMethodTranslation from './shipping-method-translation';
import StockMovement from './stock-movement';
import Surcharge from './surcharge';
import Tag from './tag';
import TaxCategory from './tax-category';
import TaxRate from './tax-rate';
import Zone from './zone';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}address`} component={Address} />
      <ErrorBoundaryRoute path={`${match.url}administrator`} component={Administrator} />
      <ErrorBoundaryRoute path={`${match.url}asset`} component={Asset} />
      <ErrorBoundaryRoute path={`${match.url}channel`} component={Channel} />
      <ErrorBoundaryRoute path={`${match.url}collection`} component={Collection} />
      <ErrorBoundaryRoute path={`${match.url}collection-asset`} component={CollectionAsset} />
      <ErrorBoundaryRoute path={`${match.url}collection-translation`} component={CollectionTranslation} />
      <ErrorBoundaryRoute path={`${match.url}country`} component={Country} />
      <ErrorBoundaryRoute path={`${match.url}country-translation`} component={CountryTranslation} />
      <ErrorBoundaryRoute path={`${match.url}customer`} component={Customer} />
      <ErrorBoundaryRoute path={`${match.url}customer-group`} component={CustomerGroup} />
      <ErrorBoundaryRoute path={`${match.url}example-entity`} component={ExampleEntity} />
      <ErrorBoundaryRoute path={`${match.url}facet`} component={Facet} />
      <ErrorBoundaryRoute path={`${match.url}facet-translation`} component={FacetTranslation} />
      <ErrorBoundaryRoute path={`${match.url}facet-value`} component={FacetValue} />
      <ErrorBoundaryRoute path={`${match.url}facet-value-translation`} component={FacetValueTranslation} />
      <ErrorBoundaryRoute path={`${match.url}fulfillment`} component={Fulfillment} />
      <ErrorBoundaryRoute path={`${match.url}global-settings`} component={GlobalSettings} />
      <ErrorBoundaryRoute path={`${match.url}history-entry`} component={HistoryEntry} />
      <ErrorBoundaryRoute path={`${match.url}job-record`} component={JobRecord} />
      <ErrorBoundaryRoute path={`${match.url}jorder`} component={Jorder} />
      <ErrorBoundaryRoute path={`${match.url}order-item`} component={OrderItem} />
      <ErrorBoundaryRoute path={`${match.url}order-line`} component={OrderLine} />
      <ErrorBoundaryRoute path={`${match.url}order-modification`} component={OrderModification} />
      <ErrorBoundaryRoute path={`${match.url}payment`} component={Payment} />
      <ErrorBoundaryRoute path={`${match.url}payment-method`} component={PaymentMethod} />
      <ErrorBoundaryRoute path={`${match.url}product`} component={Product} />
      <ErrorBoundaryRoute path={`${match.url}product-asset`} component={ProductAsset} />
      <ErrorBoundaryRoute path={`${match.url}product-option`} component={ProductOption} />
      <ErrorBoundaryRoute path={`${match.url}product-option-group`} component={ProductOptionGroup} />
      <ErrorBoundaryRoute path={`${match.url}pogt`} component={Pogt} />
      <ErrorBoundaryRoute path={`${match.url}product-option-translation`} component={ProductOptionTranslation} />
      <ErrorBoundaryRoute path={`${match.url}product-translation`} component={ProductTranslation} />
      <ErrorBoundaryRoute path={`${match.url}product-variant`} component={ProductVariant} />
      <ErrorBoundaryRoute path={`${match.url}product-variant-asset`} component={ProductVariantAsset} />
      <ErrorBoundaryRoute path={`${match.url}product-variant-price`} component={ProductVariantPrice} />
      <ErrorBoundaryRoute path={`${match.url}product-variant-translation`} component={ProductVariantTranslation} />
      <ErrorBoundaryRoute path={`${match.url}promotion`} component={Promotion} />
      <ErrorBoundaryRoute path={`${match.url}refund`} component={Refund} />
      <ErrorBoundaryRoute path={`${match.url}shipping-line`} component={ShippingLine} />
      <ErrorBoundaryRoute path={`${match.url}shipping-method`} component={ShippingMethod} />
      <ErrorBoundaryRoute path={`${match.url}shipping-method-translation`} component={ShippingMethodTranslation} />
      <ErrorBoundaryRoute path={`${match.url}stock-movement`} component={StockMovement} />
      <ErrorBoundaryRoute path={`${match.url}surcharge`} component={Surcharge} />
      <ErrorBoundaryRoute path={`${match.url}tag`} component={Tag} />
      <ErrorBoundaryRoute path={`${match.url}tax-category`} component={TaxCategory} />
      <ErrorBoundaryRoute path={`${match.url}tax-rate`} component={TaxRate} />
      <ErrorBoundaryRoute path={`${match.url}zone`} component={Zone} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
