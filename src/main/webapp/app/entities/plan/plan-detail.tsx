import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './plan.reducer';

export const PlanDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const planEntity = useAppSelector(state => state.plan.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="planDetailsHeading">
          <Translate contentKey="gymPlusApp.plan.detail.title">Plan</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="gymPlusApp.plan.id">Id</Translate>
            </span>
          </dt>
          <dd>{planEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="gymPlusApp.plan.name">Name</Translate>
            </span>
          </dt>
          <dd>{planEntity.name}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="gymPlusApp.plan.price">Price</Translate>
            </span>
          </dt>
          <dd>{planEntity.price}</dd>
          <dt>
            <Translate contentKey="gymPlusApp.plan.periods">Periods</Translate>
          </dt>
          <dd>{planEntity.periods ? planEntity.periods.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/plan" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/plan/${planEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PlanDetail;
