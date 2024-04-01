import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order-unit.reducer';

export const OrderUnitDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const orderUnitEntity = useAppSelector(state => state.orderUnit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderUnitDetailsHeading">
          <Translate contentKey="gymPlusApp.orderUnit.detail.title">OrderUnit</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="gymPlusApp.orderUnit.id">Id</Translate>
            </span>
          </dt>
          <dd>{orderUnitEntity.id}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="gymPlusApp.orderUnit.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{orderUnitEntity.quantity}</dd>
          <dt>
            <span id="unitPrice">
              <Translate contentKey="gymPlusApp.orderUnit.unitPrice">Unit Price</Translate>
            </span>
          </dt>
          <dd>{orderUnitEntity.unitPrice}</dd>
          <dt>
            <Translate contentKey="gymPlusApp.orderUnit.orderUnits">Order Units</Translate>
          </dt>
          <dd>{orderUnitEntity.orderUnits ? orderUnitEntity.orderUnits.id : ''}</dd>
          <dt>
            <Translate contentKey="gymPlusApp.orderUnit.products">Products</Translate>
          </dt>
          <dd>{orderUnitEntity.products ? orderUnitEntity.products.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-unit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-unit/${orderUnitEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderUnitDetail;
