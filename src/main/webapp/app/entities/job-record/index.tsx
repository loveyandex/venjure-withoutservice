import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import JobRecord from './job-record';
import JobRecordDetail from './job-record-detail';
import JobRecordUpdate from './job-record-update';
import JobRecordDeleteDialog from './job-record-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={JobRecordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={JobRecordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={JobRecordDetail} />
      <ErrorBoundaryRoute path={match.url} component={JobRecord} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={JobRecordDeleteDialog} />
  </>
);

export default Routes;
