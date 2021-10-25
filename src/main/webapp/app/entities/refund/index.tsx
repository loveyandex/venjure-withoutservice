import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Refund from './refund';
import RefundDetail from './refund-detail';
import RefundUpdate from './refund-update';
import RefundDeleteDialog from './refund-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RefundUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RefundUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RefundDetail} />
      <ErrorBoundaryRoute path={match.url} component={Refund} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RefundDeleteDialog} />
  </>
);

export default Routes;
