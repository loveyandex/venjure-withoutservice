import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShippingMethod from './shipping-method';
import ShippingMethodDetail from './shipping-method-detail';
import ShippingMethodUpdate from './shipping-method-update';
import ShippingMethodDeleteDialog from './shipping-method-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShippingMethodUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShippingMethodUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShippingMethodDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShippingMethod} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ShippingMethodDeleteDialog} />
  </>
);

export default Routes;
