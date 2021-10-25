import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Jorder from './jorder';
import JorderDetail from './jorder-detail';
import JorderUpdate from './jorder-update';
import JorderDeleteDialog from './jorder-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={JorderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={JorderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={JorderDetail} />
      <ErrorBoundaryRoute path={match.url} component={Jorder} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={JorderDeleteDialog} />
  </>
);

export default Routes;
