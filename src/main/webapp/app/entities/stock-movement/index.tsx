import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import StockMovement from './stock-movement';
import StockMovementDetail from './stock-movement-detail';
import StockMovementUpdate from './stock-movement-update';
import StockMovementDeleteDialog from './stock-movement-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StockMovementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StockMovementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StockMovementDetail} />
      <ErrorBoundaryRoute path={match.url} component={StockMovement} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={StockMovementDeleteDialog} />
  </>
);

export default Routes;
