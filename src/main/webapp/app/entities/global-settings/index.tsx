import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GlobalSettings from './global-settings';
import GlobalSettingsDetail from './global-settings-detail';
import GlobalSettingsUpdate from './global-settings-update';
import GlobalSettingsDeleteDialog from './global-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GlobalSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GlobalSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GlobalSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={GlobalSettings} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GlobalSettingsDeleteDialog} />
  </>
);

export default Routes;
