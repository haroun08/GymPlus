import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PeriodicSubscription from './periodic-subscription';
import PeriodicSubscriptionDetail from './periodic-subscription-detail';
import PeriodicSubscriptionUpdate from './periodic-subscription-update';
import PeriodicSubscriptionDeleteDialog from './periodic-subscription-delete-dialog';

const PeriodicSubscriptionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PeriodicSubscription />} />
    <Route path="new" element={<PeriodicSubscriptionUpdate />} />
    <Route path=":id">
      <Route index element={<PeriodicSubscriptionDetail />} />
      <Route path="edit" element={<PeriodicSubscriptionUpdate />} />
      <Route path="delete" element={<PeriodicSubscriptionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PeriodicSubscriptionRoutes;
