import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OrderLine from './order-line';
import OrderLineDetail from './order-line-detail';
import OrderLineUpdate from './order-line-update';
import OrderLineDeleteDialog from './order-line-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OrderLineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OrderLineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OrderLineDetail} />
      <ErrorBoundaryRoute path={match.url} component={OrderLine} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OrderLineDeleteDialog} />
  </>
);

export default Routes;
