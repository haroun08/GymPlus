import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProductHistory from './product-history';
import ProductHistoryDetail from './product-history-detail';
import ProductHistoryUpdate from './product-history-update';
import ProductHistoryDeleteDialog from './product-history-delete-dialog';

const ProductHistoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProductHistory />} />
    <Route path="new" element={<ProductHistoryUpdate />} />
    <Route path=":id">
      <Route index element={<ProductHistoryDetail />} />
      <Route path="edit" element={<ProductHistoryUpdate />} />
      <Route path="delete" element={<ProductHistoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProductHistoryRoutes;
