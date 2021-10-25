import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductVariantAsset from './product-variant-asset';
import ProductVariantAssetDetail from './product-variant-asset-detail';
import ProductVariantAssetUpdate from './product-variant-asset-update';
import ProductVariantAssetDeleteDialog from './product-variant-asset-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductVariantAssetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductVariantAssetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductVariantAssetDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductVariantAsset} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductVariantAssetDeleteDialog} />
  </>
);

export default Routes;
