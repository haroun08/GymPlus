import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './periodic-subscription.reducer';

export const PeriodicSubscriptionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const periodicSubscriptionEntity = useAppSelector(state => state.periodicSubscription.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="periodicSubscriptionDetailsHeading">
          <Translate contentKey="gymPlusApp.periodicSubscription.detail.title">PeriodicSubscription</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="gymPlusApp.periodicSubscription.id">Id</Translate>
            </span>
          </dt>
          <dd>{periodicSubscriptionEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="gymPlusApp.periodicSubscription.name">Name</Translate>
            </span>
          </dt>
          <dd>{periodicSubscriptionEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="gymPlusApp.periodicSubscription.description">Description</Translate>
            </span>
          </dt>
          <dd>{periodicSubscriptionEntity.description}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gymPlusApp.periodicSubscription.status">Status</Translate>
            </span>
          </dt>
          <dd>{periodicSubscriptionEntity.status ? 'true' : 'false'}</dd>
          <dt>
            <span id="paymentStatus">
              <Translate contentKey="gymPlusApp.periodicSubscription.paymentStatus">Payment Status</Translate>
            </span>
          </dt>
          <dd>{periodicSubscriptionEntity.paymentStatus ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="gymPlusApp.periodicSubscription.id">Id</Translate>
          </dt>
          <dd>
            {periodicSubscriptionEntity.ids
              ? periodicSubscriptionEntity.ids.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {periodicSubscriptionEntity.ids && i === periodicSubscriptionEntity.ids.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="gymPlusApp.periodicSubscription.plans">Plans</Translate>
          </dt>
          <dd>{periodicSubscriptionEntity.plans ? periodicSubscriptionEntity.plans.id : ''}</dd>
          <dt>
            <Translate contentKey="gymPlusApp.periodicSubscription.invoices">Invoices</Translate>
          </dt>
          <dd>{periodicSubscriptionEntity.invoices ? periodicSubscriptionEntity.invoices.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/periodic-subscription" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/periodic-subscription/${periodicSubscriptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PeriodicSubscriptionDetail;
