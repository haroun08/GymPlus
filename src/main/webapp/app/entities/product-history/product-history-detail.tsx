import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-history.reducer';

export const ProductHistoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productHistoryEntity = useAppSelector(state => state.productHistory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productHistoryDetailsHeading">
          <Translate contentKey="gymPlusApp.productHistory.detail.title">ProductHistory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="gymPlusApp.productHistory.id">Id</Translate>
            </span>
          </dt>
          <dd>{productHistoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="gymPlusApp.productHistory.name">Name</Translate>
            </span>
          </dt>
          <dd>{productHistoryEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="gymPlusApp.productHistory.description">Description</Translate>
            </span>
          </dt>
          <dd>{productHistoryEntity.description}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="gymPlusApp.productHistory.price">Price</Translate>
            </span>
          </dt>
          <dd>{productHistoryEntity.price}</dd>
          <dt>
            <span id="availableStockQuantity">
              <Translate contentKey="gymPlusApp.productHistory.availableStockQuantity">Available Stock Quantity</Translate>
            </span>
          </dt>
          <dd>{productHistoryEntity.availableStockQuantity}</dd>
          <dt>
            <span id="validFrom">
              <Translate contentKey="gymPlusApp.productHistory.validFrom">Valid From</Translate>
            </span>
          </dt>
          <dd>
            {productHistoryEntity.validFrom ? (
              <TextFormat value={productHistoryEntity.validFrom} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="validTo">
              <Translate contentKey="gymPlusApp.productHistory.validTo">Valid To</Translate>
            </span>
          </dt>
          <dd>
            {productHistoryEntity.validTo ? <TextFormat value={productHistoryEntity.validTo} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="gymPlusApp.productHistory.product">Product</Translate>
          </dt>
          <dd>{productHistoryEntity.product ? productHistoryEntity.product.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product-history" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-history/${productHistoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductHistoryDetail;
