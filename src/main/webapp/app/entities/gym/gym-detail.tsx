import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './gym.reducer';

export const GymDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const gymEntity = useAppSelector(state => state.gym.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gymDetailsHeading">
          <Translate contentKey="gymPlusApp.gym.detail.title">Gym</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="gymPlusApp.gym.id">Id</Translate>
            </span>
          </dt>
          <dd>{gymEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="gymPlusApp.gym.name">Name</Translate>
            </span>
          </dt>
          <dd>{gymEntity.name}</dd>
          <dt>
            <span id="streetAddress">
              <Translate contentKey="gymPlusApp.gym.streetAddress">Street Address</Translate>
            </span>
          </dt>
          <dd>{gymEntity.streetAddress}</dd>
          <dt>
            <span id="postalCode">
              <Translate contentKey="gymPlusApp.gym.postalCode">Postal Code</Translate>
            </span>
          </dt>
          <dd>{gymEntity.postalCode}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="gymPlusApp.gym.city">City</Translate>
            </span>
          </dt>
          <dd>{gymEntity.city}</dd>
          <dt>
            <span id="stateProvince">
              <Translate contentKey="gymPlusApp.gym.stateProvince">State Province</Translate>
            </span>
          </dt>
          <dd>{gymEntity.stateProvince}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="gymPlusApp.gym.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{gymEntity.phoneNumber}</dd>
          <dt>
            <Translate contentKey="gymPlusApp.gym.id">Id</Translate>
          </dt>
          <dd>
            {gymEntity.ids
              ? gymEntity.ids.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {gymEntity.ids && i === gymEntity.ids.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/gym" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/gym/${gymEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GymDetail;
