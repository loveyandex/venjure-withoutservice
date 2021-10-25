import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OrderModification from './order-modification';
import OrderModificationDetail from './order-modification-detail';
import OrderModificationUpdate from './order-modification-update';
import OrderModificationDeleteDialog from './order-modification-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OrderModificationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OrderModificationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OrderModificationDetail} />
      <ErrorBoundaryRoute path={match.url} component={OrderModification} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OrderModificationDeleteDialog} />
  </>
);

export default Routes;
