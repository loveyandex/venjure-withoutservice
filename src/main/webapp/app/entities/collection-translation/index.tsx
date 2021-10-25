import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CollectionTranslation from './collection-translation';
import CollectionTranslationDetail from './collection-translation-detail';
import CollectionTranslationUpdate from './collection-translation-update';
import CollectionTranslationDeleteDialog from './collection-translation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CollectionTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CollectionTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CollectionTranslationDetail} />
      <ErrorBoundaryRoute path={match.url} component={CollectionTranslation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CollectionTranslationDeleteDialog} />
  </>
);

export default Routes;
