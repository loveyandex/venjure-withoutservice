import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HistoryEntry from './history-entry';
import HistoryEntryDetail from './history-entry-detail';
import HistoryEntryUpdate from './history-entry-update';
import HistoryEntryDeleteDialog from './history-entry-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HistoryEntryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HistoryEntryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HistoryEntryDetail} />
      <ErrorBoundaryRoute path={match.url} component={HistoryEntry} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HistoryEntryDeleteDialog} />
  </>
);

export default Routes;
