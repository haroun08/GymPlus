import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOrder } from 'app/shared/model/order.model';
import { getEntities as getOrders } from 'app/entities/order/order.reducer';
import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IOrderUnit } from 'app/shared/model/order-unit.model';
import { getEntity, updateEntity, createEntity, reset } from './order-unit.reducer';

export const OrderUnitUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const orders = useAppSelector(state => state.order.entities);
  const products = useAppSelector(state => state.product.entities);
  const orderUnitEntity = useAppSelector(state => state.orderUnit.entity);
  const loading = useAppSelector(state => state.orderUnit.loading);
  const updating = useAppSelector(state => state.orderUnit.updating);
  const updateSuccess = useAppSelector(state => state.orderUnit.updateSuccess);

  const handleClose = () => {
    navigate('/order-unit');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getOrders({}));
    dispatch(getProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.quantity !== undefined && typeof values.quantity !== 'number') {
      values.quantity = Number(values.quantity);
    }
    if (values.unitPrice !== undefined && typeof values.unitPrice !== 'number') {
      values.unitPrice = Number(values.unitPrice);
    }

    const entity = {
      ...orderUnitEntity,
      ...values,
      orderUnits: orders.find(it => it.id.toString() === values.orderUnits?.toString()),
      products: products.find(it => it.id.toString() === values.products?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...orderUnitEntity,
          orderUnits: orderUnitEntity?.orderUnits?.id,
          products: orderUnitEntity?.products?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gymPlusApp.orderUnit.home.createOrEditLabel" data-cy="OrderUnitCreateUpdateHeading">
            <Translate contentKey="gymPlusApp.orderUnit.home.createOrEditLabel">Create or edit a OrderUnit</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="order-unit-id"
                  label={translate('gymPlusApp.orderUnit.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gymPlusApp.orderUnit.quantity')}
                id="order-unit-quantity"
                name="quantity"
                data-cy="quantity"
                type="text"
              />
              <ValidatedField
                label={translate('gymPlusApp.orderUnit.unitPrice')}
                id="order-unit-unitPrice"
                name="unitPrice"
                data-cy="unitPrice"
                type="text"
              />
              <ValidatedField
                id="order-unit-orderUnits"
                name="orderUnits"
                data-cy="orderUnits"
                label={translate('gymPlusApp.orderUnit.orderUnits')}
                type="select"
              >
                <option value="" key="0" />
                {orders
                  ? orders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="order-unit-products"
                name="products"
                data-cy="products"
                label={translate('gymPlusApp.orderUnit.products')}
                type="select"
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/order-unit" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default OrderUnitUpdate;
