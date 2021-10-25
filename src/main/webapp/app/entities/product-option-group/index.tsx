import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductOptionGroup from './product-option-group';
import ProductOptionGroupDetail from './product-option-group-detail';
import ProductOptionGroupUpdate from './product-option-group-update';
import ProductOptionGroupDeleteDialog from './product-option-group-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductOptionGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductOptionGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductOptionGroupDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductOptionGroup} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductOptionGroupDeleteDialog} />
  </>
);

export default Routes;
