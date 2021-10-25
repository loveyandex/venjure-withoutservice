import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Asset from './asset';
import AssetDetail from './asset-detail';
import AssetUpdate from './asset-update';
import AssetDeleteDialog from './asset-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AssetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AssetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AssetDetail} />
      <ErrorBoundaryRoute path={match.url} component={Asset} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AssetDeleteDialog} />
  </>
);

export default Routes;
