import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Surcharge from './surcharge';
import SurchargeDetail from './surcharge-detail';
import SurchargeUpdate from './surcharge-update';
import SurchargeDeleteDialog from './surcharge-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SurchargeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SurchargeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SurchargeDetail} />
      <ErrorBoundaryRoute path={match.url} component={Surcharge} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SurchargeDeleteDialog} />
  </>
);

export default Routes;
