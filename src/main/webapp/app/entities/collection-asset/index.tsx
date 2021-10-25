import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CollectionAsset from './collection-asset';
import CollectionAssetDetail from './collection-asset-detail';
import CollectionAssetUpdate from './collection-asset-update';
import CollectionAssetDeleteDialog from './collection-asset-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CollectionAssetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CollectionAssetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CollectionAssetDetail} />
      <ErrorBoundaryRoute path={match.url} component={CollectionAsset} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CollectionAssetDeleteDialog} />
  </>
);

export default Routes;
