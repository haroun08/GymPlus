import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IGym } from 'app/shared/model/gym.model';
import { getEntity, updateEntity, createEntity, reset } from './gym.reducer';

export const GymUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const gymEntity = useAppSelector(state => state.gym.entity);
  const loading = useAppSelector(state => state.gym.loading);
  const updating = useAppSelector(state => state.gym.updating);
  const updateSuccess = useAppSelector(state => state.gym.updateSuccess);

  const handleClose = () => {
    navigate('/gym');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
    if (values.phoneNumber !== undefined && typeof values.phoneNumber !== 'number') {
      values.phoneNumber = Number(values.phoneNumber);
    }

    const entity = {
      ...gymEntity,
      ...values,
      ids: mapIdList(values.ids),
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
          ...gymEntity,
          ids: gymEntity?.ids?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gymPlusApp.gym.home.createOrEditLabel" data-cy="GymCreateUpdateHeading">
            <Translate contentKey="gymPlusApp.gym.home.createOrEditLabel">Create or edit a Gym</Translate>
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
                  id="gym-id"
                  label={translate('gymPlusApp.gym.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('gymPlusApp.gym.name')} id="gym-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('gymPlusApp.gym.streetAddress')}
                id="gym-streetAddress"
                name="streetAddress"
                data-cy="streetAddress"
                type="text"
              />
              <ValidatedField
                label={translate('gymPlusApp.gym.postalCode')}
                id="gym-postalCode"
                name="postalCode"
                data-cy="postalCode"
                type="text"
              />
              <ValidatedField label={translate('gymPlusApp.gym.city')} id="gym-city" name="city" data-cy="city" type="text" />
              <ValidatedField
                label={translate('gymPlusApp.gym.stateProvince')}
                id="gym-stateProvince"
                name="stateProvince"
                data-cy="stateProvince"
                type="text"
              />
              <ValidatedField
                label={translate('gymPlusApp.gym.phoneNumber')}
                id="gym-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
              />
              <ValidatedField label={translate('gymPlusApp.gym.id')} id="gym-id" data-cy="id" type="select" multiple name="ids">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/gym" replace color="info">
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

export default GymUpdate;
