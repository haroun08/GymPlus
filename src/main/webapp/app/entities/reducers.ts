import category from 'app/entities/category/category.reducer';
import gym from 'app/entities/gym/gym.reducer';
import invoice from 'app/entities/invoice/invoice.reducer';
import order from 'app/entities/order/order.reducer';
import orderUnit from 'app/entities/order-unit/order-unit.reducer';
import payment from 'app/entities/payment/payment.reducer';
import period from 'app/entities/period/period.reducer';
import periodicSubscription from 'app/entities/periodic-subscription/periodic-subscription.reducer';
import plan from 'app/entities/plan/plan.reducer';
import product from 'app/entities/product/product.reducer';
import productHistory from 'app/entities/product-history/product-history.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */
import chatroomReducer from './chat/chatroom.reducer';
const entitiesReducers = {
  chatroomReducer,
  category,
  gym,
  invoice,
  order,
  orderUnit,
  payment,
  period,
  periodicSubscription,
  plan,
  product,
  productHistory,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
