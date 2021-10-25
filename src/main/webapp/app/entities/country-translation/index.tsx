import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CountryTranslation from './country-translation';
import CountryTranslationDetail from './country-translation-detail';
import CountryTranslationUpdate from './country-translation-update';
import CountryTranslationDeleteDialog from './country-translation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CountryTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CountryTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CountryTranslationDetail} />
      <ErrorBoundaryRoute path={match.url} component={CountryTranslation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CountryTranslationDeleteDialog} />
  </>
);

export default Routes;
