import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/category">
        <Translate contentKey="global.menu.entities.category" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/gym">
        <Translate contentKey="global.menu.entities.gym" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/invoice">
        <Translate contentKey="global.menu.entities.invoice" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order">
        <Translate contentKey="global.menu.entities.order" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order-unit">
        <Translate contentKey="global.menu.entities.orderUnit" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment">
        <Translate contentKey="global.menu.entities.payment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/period">
        <Translate contentKey="global.menu.entities.period" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/periodic-subscription">
        <Translate contentKey="global.menu.entities.periodicSubscription" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/plan">
        <Translate contentKey="global.menu.entities.plan" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/product">
        <Translate contentKey="global.menu.entities.product" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/product-history">
        <Translate contentKey="global.menu.entities.productHistory" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
      <MenuItem icon="comment-alt" to="/chat" data-cy="chatMenuItem">
        Chat
      </MenuItem>
    </>
  );
};

export default EntitiesMenu;
