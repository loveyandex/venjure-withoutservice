import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Fulfillment from './fulfillment';
import FulfillmentDetail from './fulfillment-detail';
import FulfillmentUpdate from './fulfillment-update';
import FulfillmentDeleteDialog from './fulfillment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FulfillmentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FulfillmentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FulfillmentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Fulfillment} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FulfillmentDeleteDialog} />
  </>
);

export default Routes;
