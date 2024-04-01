import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import OrderUnit from './order-unit';
import OrderUnitDetail from './order-unit-detail';
import OrderUnitUpdate from './order-unit-update';
import OrderUnitDeleteDialog from './order-unit-delete-dialog';

const OrderUnitRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<OrderUnit />} />
    <Route path="new" element={<OrderUnitUpdate />} />
    <Route path=":id">
      <Route index element={<OrderUnitDetail />} />
      <Route path="edit" element={<OrderUnitUpdate />} />
      <Route path="delete" element={<OrderUnitDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrderUnitRoutes;
