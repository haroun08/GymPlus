import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPeriod } from 'app/shared/model/period.model';
import { getEntities as getPeriods } from 'app/entities/period/period.reducer';
import { IPlan } from 'app/shared/model/plan.model';
import { getEntity, updateEntity, createEntity, reset } from './plan.reducer';

export const PlanUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const periods = useAppSelector(state => state.period.entities);
  const planEntity = useAppSelector(state => state.plan.entity);
  const loading = useAppSelector(state => state.plan.loading);
  const updating = useAppSelector(state => state.plan.updating);
  const updateSuccess = useAppSelector(state => state.plan.updateSuccess);

  const handleClose = () => {
    navigate('/plan');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPeriods({}));
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

    const entity = {
      ...planEntity,
      ...values,
      periods: periods.find(it => it.id.toString() === values.periods?.toString()),
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
          ...planEntity,
          periods: planEntity?.periods?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gymPlusApp.plan.home.createOrEditLabel" data-cy="PlanCreateUpdateHeading">
            <Translate contentKey="gymPlusApp.plan.home.createOrEditLabel">Create or edit a Plan</Translate>
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
                  id="plan-id"
                  label={translate('gymPlusApp.plan.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('gymPlusApp.plan.name')} id="plan-name" name="name" data-cy="name" type="text" />
              <ValidatedField label={translate('gymPlusApp.plan.price')} id="plan-price" name="price" data-cy="price" type="text" />
              <ValidatedField id="plan-periods" name="periods" data-cy="periods" label={translate('gymPlusApp.plan.periods')} type="select">
                <option value="" key="0" />
                {periods
                  ? periods.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/plan" replace color="info">
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

export default PlanUpdate;
