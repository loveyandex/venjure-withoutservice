import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Pogt from './pogt';
import PogtDetail from './pogt-detail';
import PogtUpdate from './pogt-update';
import PogtDeleteDialog from './pogt-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PogtUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PogtUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PogtDetail} />
      <ErrorBoundaryRoute path={match.url} component={Pogt} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PogtDeleteDialog} />
  </>
);

export default Routes;
