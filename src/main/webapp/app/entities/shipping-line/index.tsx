import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShippingLine from './shipping-line';
import ShippingLineDetail from './shipping-line-detail';
import ShippingLineUpdate from './shipping-line-update';
import ShippingLineDeleteDialog from './shipping-line-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShippingLineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShippingLineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShippingLineDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShippingLine} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ShippingLineDeleteDialog} />
  </>
);

export default Routes;
