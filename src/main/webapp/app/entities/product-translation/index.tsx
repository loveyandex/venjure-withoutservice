import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductTranslation from './product-translation';
import ProductTranslationDetail from './product-translation-detail';
import ProductTranslationUpdate from './product-translation-update';
import ProductTranslationDeleteDialog from './product-translation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductTranslationDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductTranslation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductTranslationDeleteDialog} />
  </>
);

export default Routes;
