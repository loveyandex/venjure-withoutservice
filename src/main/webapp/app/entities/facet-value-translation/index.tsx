import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FacetValueTranslation from './facet-value-translation';
import FacetValueTranslationDetail from './facet-value-translation-detail';
import FacetValueTranslationUpdate from './facet-value-translation-update';
import FacetValueTranslationDeleteDialog from './facet-value-translation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FacetValueTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FacetValueTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FacetValueTranslationDetail} />
      <ErrorBoundaryRoute path={match.url} component={FacetValueTranslation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FacetValueTranslationDeleteDialog} />
  </>
);

export default Routes;
