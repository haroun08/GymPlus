import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IProductHistory } from 'app/shared/model/product-history.model';
import { getEntity, updateEntity, createEntity, reset } from './product-history.reducer';

export const ProductHistoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const products = useAppSelector(state => state.product.entities);
  const productHistoryEntity = useAppSelector(state => state.productHistory.entity);
  const loading = useAppSelector(state => state.productHistory.loading);
  const updating = useAppSelector(state => state.productHistory.updating);
  const updateSuccess = useAppSelector(state => state.productHistory.updateSuccess);

  const handleClose = () => {
    navigate('/product-history');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    if (values.availableStockQuantity !== undefined && typeof values.availableStockQuantity !== 'number') {
      values.availableStockQuantity = Number(values.availableStockQuantity);
    }
    values.validFrom = convertDateTimeToServer(values.validFrom);
    values.validTo = convertDateTimeToServer(values.validTo);

    const entity = {
      ...productHistoryEntity,
      ...values,
      product: products.find(it => it.id.toString() === values.product?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          validFrom: displayDefaultDateTime(),
          validTo: displayDefaultDateTime(),
        }
      : {
          ...productHistoryEntity,
          validFrom: convertDateTimeFromServer(productHistoryEntity.validFrom),
          validTo: convertDateTimeFromServer(productHistoryEntity.validTo),
          product: productHistoryEntity?.product?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gymPlusApp.productHistory.home.createOrEditLabel" data-cy="ProductHistoryCreateUpdateHeading">
            <Translate contentKey="gymPlusApp.productHistory.home.createOrEditLabel">Create or edit a ProductHistory</Translate>
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
                  id="product-history-id"
                  label={translate('gymPlusApp.productHistory.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gymPlusApp.productHistory.name')}
                id="product-history-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('gymPlusApp.productHistory.description')}
                id="product-history-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('gymPlusApp.productHistory.price')}
                id="product-history-price"
                name="price"
                data-cy="price"
                type="text"
              />
              <ValidatedField
                label={translate('gymPlusApp.productHistory.availableStockQuantity')}
                id="product-history-availableStockQuantity"
                name="availableStockQuantity"
                data-cy="availableStockQuantity"
                type="text"
              />
              <ValidatedField
                label={translate('gymPlusApp.productHistory.validFrom')}
                id="product-history-validFrom"
                name="validFrom"
                data-cy="validFrom"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('gymPlusApp.productHistory.validTo')}
                id="product-history-validTo"
                name="validTo"
                data-cy="validTo"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="product-history-product"
                name="product"
                data-cy="product"
                label={translate('gymPlusApp.productHistory.product')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-history" replace color="info">
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

export default ProductHistoryUpdate;
