import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TaxRate from './tax-rate';
import TaxRateDetail from './tax-rate-detail';
import TaxRateUpdate from './tax-rate-update';
import TaxRateDeleteDialog from './tax-rate-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TaxRateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TaxRateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TaxRateDetail} />
      <ErrorBoundaryRoute path={match.url} component={TaxRate} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TaxRateDeleteDialog} />
  </>
);

export default Routes;
