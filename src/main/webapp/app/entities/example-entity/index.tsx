import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ExampleEntity from './example-entity';
import ExampleEntityDetail from './example-entity-detail';
import ExampleEntityUpdate from './example-entity-update';
import ExampleEntityDeleteDialog from './example-entity-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExampleEntityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExampleEntityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExampleEntityDetail} />
      <ErrorBoundaryRoute path={match.url} component={ExampleEntity} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ExampleEntityDeleteDialog} />
  </>
);

export default Routes;
