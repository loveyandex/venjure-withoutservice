import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/address">
      <Translate contentKey="global.menu.entities.address" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/administrator">
      <Translate contentKey="global.menu.entities.administrator" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/asset">
      <Translate contentKey="global.menu.entities.asset" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/channel">
      <Translate contentKey="global.menu.entities.channel" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/collection">
      <Translate contentKey="global.menu.entities.collection" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/collection-asset">
      <Translate contentKey="global.menu.entities.collectionAsset" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/collection-translation">
      <Translate contentKey="global.menu.entities.collectionTranslation" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/country">
      <Translate contentKey="global.menu.entities.country" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/country-translation">
      <Translate contentKey="global.menu.entities.countryTranslation" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/customer">
      <Translate contentKey="global.menu.entities.customer" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/customer-group">
      <Translate contentKey="global.menu.entities.customerGroup" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/example-entity">
      <Translate contentKey="global.menu.entities.exampleEntity" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/facet">
      <Translate contentKey="global.menu.entities.facet" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/facet-translation">
      <Translate contentKey="global.menu.entities.facetTranslation" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/facet-value">
      <Translate contentKey="global.menu.entities.facetValue" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/facet-value-translation">
      <Translate contentKey="global.menu.entities.facetValueTranslation" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/fulfillment">
      <Translate contentKey="global.menu.entities.fulfillment" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/global-settings">
      <Translate contentKey="global.menu.entities.globalSettings" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/history-entry">
      <Translate contentKey="global.menu.entities.historyEntry" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/job-record">
      <Translate contentKey="global.menu.entities.jobRecord" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/jorder">
      <Translate contentKey="global.menu.entities.jorder" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/order-item">
      <Translate contentKey="global.menu.entities.orderItem" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/order-line">
      <Translate contentKey="global.menu.entities.orderLine" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/order-modification">
      <Translate contentKey="global.menu.entities.orderModification" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/payment">
      <Translate contentKey="global.menu.entities.payment" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/payment-method">
      <Translate contentKey="global.menu.entities.paymentMethod" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/product">
      <Translate contentKey="global.menu.entities.product" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-asset">
      <Translate contentKey="global.menu.entities.productAsset" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-option">
      <Translate contentKey="global.menu.entities.productOption" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-option-group">
      <Translate contentKey="global.menu.entities.productOptionGroup" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/pogt">
      <Translate contentKey="global.menu.entities.pogt" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-option-translation">
      <Translate contentKey="global.menu.entities.productOptionTranslation" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-translation">
      <Translate contentKey="global.menu.entities.productTranslation" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-variant">
      <Translate contentKey="global.menu.entities.productVariant" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-variant-asset">
      <Translate contentKey="global.menu.entities.productVariantAsset" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-variant-price">
      <Translate contentKey="global.menu.entities.productVariantPrice" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-variant-translation">
      <Translate contentKey="global.menu.entities.productVariantTranslation" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/promotion">
      <Translate contentKey="global.menu.entities.promotion" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/refund">
      <Translate contentKey="global.menu.entities.refund" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/shipping-line">
      <Translate contentKey="global.menu.entities.shippingLine" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/shipping-method">
      <Translate contentKey="global.menu.entities.shippingMethod" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/shipping-method-translation">
      <Translate contentKey="global.menu.entities.shippingMethodTranslation" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/stock-movement">
      <Translate contentKey="global.menu.entities.stockMovement" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/surcharge">
      <Translate contentKey="global.menu.entities.surcharge" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/tag">
      <Translate contentKey="global.menu.entities.tag" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/tax-category">
      <Translate contentKey="global.menu.entities.taxCategory" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/tax-rate">
      <Translate contentKey="global.menu.entities.taxRate" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/zone">
      <Translate contentKey="global.menu.entities.zone" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
