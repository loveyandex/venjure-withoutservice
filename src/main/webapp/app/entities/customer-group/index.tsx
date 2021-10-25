import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CustomerGroup from './customer-group';
import CustomerGroupDetail from './customer-group-detail';
import CustomerGroupUpdate from './customer-group-update';
import CustomerGroupDeleteDialog from './customer-group-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CustomerGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CustomerGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CustomerGroupDetail} />
      <ErrorBoundaryRoute path={match.url} component={CustomerGroup} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CustomerGroupDeleteDialog} />
  </>
);

export default Routes;
