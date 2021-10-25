import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductAsset from './product-asset';
import ProductAssetDetail from './product-asset-detail';
import ProductAssetUpdate from './product-asset-update';
import ProductAssetDeleteDialog from './product-asset-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductAssetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductAssetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductAssetDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductAsset} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductAssetDeleteDialog} />
  </>
);

export default Routes;
