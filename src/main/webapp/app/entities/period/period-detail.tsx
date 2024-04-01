import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './period.reducer';

export const PeriodDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const periodEntity = useAppSelector(state => state.period.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="periodDetailsHeading">
          <Translate contentKey="gymPlusApp.period.detail.title">Period</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="gymPlusApp.period.id">Id</Translate>
            </span>
          </dt>
          <dd>{periodEntity.id}</dd>
          <dt>
            <span id="monthOccurrence">
              <Translate contentKey="gymPlusApp.period.monthOccurrence">Month Occurrence</Translate>
            </span>
          </dt>
          <dd>{periodEntity.monthOccurrence}</dd>
          <dt>
            <span id="dayOccurrence">
              <Translate contentKey="gymPlusApp.period.dayOccurrence">Day Occurrence</Translate>
            </span>
          </dt>
          <dd>{periodEntity.dayOccurrence}</dd>
        </dl>
        <Button tag={Link} to="/period" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/period/${periodEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PeriodDetail;
