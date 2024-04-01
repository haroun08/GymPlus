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
import { IPlan } from 'app/shared/model/plan.model';
import { getEntities as getPlans } from 'app/entities/plan/plan.reducer';
import { IInvoice } from 'app/shared/model/invoice.model';
import { getEntities as getInvoices } from 'app/entities/invoice/invoice.reducer';
import { IPeriodicSubscription } from 'app/shared/model/periodic-subscription.model';
import { getEntity, updateEntity, createEntity, reset } from './periodic-subscription.reducer';

export const PeriodicSubscriptionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const plans = useAppSelector(state => state.plan.entities);
  const invoices = useAppSelector(state => state.invoice.entities);
  const periodicSubscriptionEntity = useAppSelector(state => state.periodicSubscription.entity);
  const loading = useAppSelector(state => state.periodicSubscription.loading);
  const updating = useAppSelector(state => state.periodicSubscription.updating);
  const updateSuccess = useAppSelector(state => state.periodicSubscription.updateSuccess);

  const handleClose = () => {
    navigate('/periodic-subscription');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getPlans({}));
    dispatch(getInvoices({}));
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

    const entity = {
      ...periodicSubscriptionEntity,
      ...values,
      ids: mapIdList(values.ids),
      plans: plans.find(it => it.id.toString() === values.plans?.toString()),
      invoices: invoices.find(it => it.id.toString() === values.invoices?.toString()),
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
          ...periodicSubscriptionEntity,
          ids: periodicSubscriptionEntity?.ids?.map(e => e.id.toString()),
          plans: periodicSubscriptionEntity?.plans?.id,
          invoices: periodicSubscriptionEntity?.invoices?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gymPlusApp.periodicSubscription.home.createOrEditLabel" data-cy="PeriodicSubscriptionCreateUpdateHeading">
            <Translate contentKey="gymPlusApp.periodicSubscription.home.createOrEditLabel">Create or edit a PeriodicSubscription</Translate>
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
                  id="periodic-subscription-id"
                  label={translate('gymPlusApp.periodicSubscription.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gymPlusApp.periodicSubscription.name')}
                id="periodic-subscription-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('gymPlusApp.periodicSubscription.description')}
                id="periodic-subscription-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('gymPlusApp.periodicSubscription.status')}
                id="periodic-subscription-status"
                name="status"
                data-cy="status"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('gymPlusApp.periodicSubscription.paymentStatus')}
                id="periodic-subscription-paymentStatus"
                name="paymentStatus"
                data-cy="paymentStatus"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('gymPlusApp.periodicSubscription.id')}
                id="periodic-subscription-id"
                data-cy="id"
                type="select"
                multiple
                name="ids"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="periodic-subscription-plans"
                name="plans"
                data-cy="plans"
                label={translate('gymPlusApp.periodicSubscription.plans')}
                type="select"
              >
                <option value="" key="0" />
                {plans
                  ? plans.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="periodic-subscription-invoices"
                name="invoices"
                data-cy="invoices"
                label={translate('gymPlusApp.periodicSubscription.invoices')}
                type="select"
              >
                <option value="" key="0" />
                {invoices
                  ? invoices.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/periodic-subscription" replace color="info">
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

export default PeriodicSubscriptionUpdate;
