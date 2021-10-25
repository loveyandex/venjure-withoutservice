import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Facet from './facet';
import FacetDetail from './facet-detail';
import FacetUpdate from './facet-update';
import FacetDeleteDialog from './facet-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FacetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FacetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FacetDetail} />
      <ErrorBoundaryRoute path={match.url} component={Facet} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FacetDeleteDialog} />
  </>
);

export default Routes;
