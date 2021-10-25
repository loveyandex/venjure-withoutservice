import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShippingMethodTranslation from './shipping-method-translation';
import ShippingMethodTranslationDetail from './shipping-method-translation-detail';
import ShippingMethodTranslationUpdate from './shipping-method-translation-update';
import ShippingMethodTranslationDeleteDialog from './shipping-method-translation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShippingMethodTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShippingMethodTranslationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShippingMethodTranslationDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShippingMethodTranslation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ShippingMethodTranslationDeleteDialog} />
  </>
);

export default Routes;
