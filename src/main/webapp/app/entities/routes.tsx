import React from 'react';
import { Route } from 'react-router-dom';
import Chatroom from './chat/Chatroom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Category from './category';
import Gym from './gym';
import Invoice from './invoice';
import Order from './order';
import OrderUnit from './order-unit';
import Payment from './payment';
import Period from './period';
import PeriodicSubscription from './periodic-subscription';
import Plan from './plan';
import Product from './product';
import ProductHistory from './product-history';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="category/*" element={<Category />} />
        <Route path="gym/*" element={<Gym />} />
        <Route path="invoice/*" element={<Invoice />} />
        <Route path="order/*" element={<Order />} />
        <Route path="order-unit/*" element={<OrderUnit />} />
        <Route path="payment/*" element={<Payment />} />
        <Route path="period/*" element={<Period />} />
        <Route path="periodic-subscription/*" element={<PeriodicSubscription />} />
        <Route path="plan/*" element={<Plan />} />
        <Route path="product/*" element={<Product />} />
        <Route path="product-history/*" element={<ProductHistory />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
        <Route path="chat" element={<Chatroom />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};
