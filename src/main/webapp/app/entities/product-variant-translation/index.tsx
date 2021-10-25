import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProductVariantTranslation from './product-variant-translation';
import ProductVariantTranslationDetail from './product-variant-translation-detail';
import ProductVariantTranslationUpdate from './product-variant-translation-update';
import ProductVariantTranslationDeleteDialog from './product-variant-translation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProductVariantTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProductVariantTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProductVariantTranslationDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProductVariantTranslation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProductVariantTranslationDeleteDialog} />
  </>
);

export default Routes;
