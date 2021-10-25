import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductOption from './product-option';
import ProductOptionDetail from './product-option-detail';
import ProductOptionUpdate from './product-option-update';
import ProductOptionDeleteDialog from './product-option-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductOptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductOptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductOptionDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductOption} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductOptionDeleteDialog} />
  </>
);

export default Routes;
