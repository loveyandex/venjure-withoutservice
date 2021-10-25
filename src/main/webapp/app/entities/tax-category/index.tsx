import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TaxCategory from './tax-category';
import TaxCategoryDetail from './tax-category-detail';
import TaxCategoryUpdate from './tax-category-update';
import TaxCategoryDeleteDialog from './tax-category-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TaxCategoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TaxCategoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TaxCategoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={TaxCategory} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TaxCategoryDeleteDialog} />
  </>
);

export default Routes;
