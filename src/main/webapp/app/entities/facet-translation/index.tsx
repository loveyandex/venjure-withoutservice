import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FacetTranslation from './facet-translation';
import FacetTranslationDetail from './facet-translation-detail';
import FacetTranslationUpdate from './facet-translation-update';
import FacetTranslationDeleteDialog from './facet-translation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FacetTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FacetTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FacetTranslationDetail} />
      <ErrorBoundaryRoute path={match.url} component={FacetTranslation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FacetTranslationDeleteDialog} />
  </>
);

export default Routes;
