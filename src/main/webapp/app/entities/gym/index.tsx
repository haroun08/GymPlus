import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Gym from './gym';
import GymDetail from './gym-detail';
import GymUpdate from './gym-update';
import GymDeleteDialog from './gym-delete-dialog';

const GymRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Gym />} />
    <Route path="new" element={<GymUpdate />} />
    <Route path=":id">
      <Route index element={<GymDetail />} />
      <Route path="edit" element={<GymUpdate />} />
      <Route path="delete" element={<GymDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GymRoutes;
