import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductVariantPrice from './product-variant-price';
import ProductVariantPriceDetail from './product-variant-price-detail';
import ProductVariantPriceUpdate from './product-variant-price-update';
import ProductVariantPriceDeleteDialog from './product-variant-price-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductVariantPriceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductVariantPriceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductVariantPriceDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductVariantPrice} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductVariantPriceDeleteDialog} />
  </>
);

export default Routes;
