import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPeriod } from 'app/shared/model/period.model';
import { getEntity, updateEntity, createEntity, reset } from './period.reducer';

export const PeriodUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const periodEntity = useAppSelector(state => state.period.entity);
  const loading = useAppSelector(state => state.period.loading);
  const updating = useAppSelector(state => state.period.updating);
  const updateSuccess = useAppSelector(state => state.period.updateSuccess);

  const handleClose = () => {
    navigate('/period');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.monthOccurrence !== undefined && typeof values.monthOccurrence !== 'number') {
      values.monthOccurrence = Number(values.monthOccurrence);
    }
    if (values.dayOccurrence !== undefined && typeof values.dayOccurrence !== 'number') {
      values.dayOccurrence = Number(values.dayOccurrence);
    }

    const entity = {
      ...periodEntity,
      ...values,
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
          ...periodEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gymPlusApp.period.home.createOrEditLabel" data-cy="PeriodCreateUpdateHeading">
            <Translate contentKey="gymPlusApp.period.home.createOrEditLabel">Create or edit a Period</Translate>
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
                  id="period-id"
                  label={translate('gymPlusApp.period.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gymPlusApp.period.monthOccurrence')}
                id="period-monthOccurrence"
                name="monthOccurrence"
                data-cy="monthOccurrence"
                type="text"
              />
              <ValidatedField
                label={translate('gymPlusApp.period.dayOccurrence')}
                id="period-dayOccurrence"
                name="dayOccurrence"
                data-cy="dayOccurrence"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/period" replace color="info">
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

export default PeriodUpdate;
