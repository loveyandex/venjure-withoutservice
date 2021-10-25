import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductOptionTranslation from './product-option-translation';
import ProductOptionTranslationDetail from './product-option-translation-detail';
import ProductOptionTranslationUpdate from './product-option-translation-update';
import ProductOptionTranslationDeleteDialog from './product-option-translation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductOptionTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductOptionTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductOptionTranslationDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductOptionTranslation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductOptionTranslationDeleteDialog} />
  </>
);

export default Routes;
