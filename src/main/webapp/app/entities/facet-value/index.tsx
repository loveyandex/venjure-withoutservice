import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FacetValue from './facet-value';
import FacetValueDetail from './facet-value-detail';
import FacetValueUpdate from './facet-value-update';
import FacetValueDeleteDialog from './facet-value-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FacetValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FacetValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FacetValueDetail} />
      <ErrorBoundaryRoute path={match.url} component={FacetValue} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FacetValueDeleteDialog} />
  </>
);

export default Routes;
